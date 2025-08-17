package sn.diti4.gestionlocationimmeubles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import sn.diti4.gestionlocationimmeubles.entity.Rapport;
import sn.diti4.gestionlocationimmeubles.repository.RapportRepository;
import sn.diti4.gestionlocationimmeubles.repository.ImmeubleRepository;
import sn.diti4.gestionlocationimmeubles.repository.UniteLocationRepository;
import sn.diti4.gestionlocationimmeubles.repository.UtilisateurRepository;
import sn.diti4.gestionlocationimmeubles.repository.ContratRepository;
import sn.diti4.gestionlocationimmeubles.repository.PaiementRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;


@Service
@RequiredArgsConstructor
public class ReportService {
    private final RapportRepository rapportRepository;
    private final SpringTemplateEngine templateEngine;
    private final ImmeubleRepository immeubleRepository;
    private final UniteLocationRepository uniteLocationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ContratRepository contratRepository;
    private final PaiementRepository paiementRepository;

    private static final String REPORTS_DIR = "generated-reports/";

    public Rapport generateReport(String type, Map<String, Object> data, String generatedBy) throws Exception {
        // Gestion du flag empty pour chaque type de rapport
        switch (type.toUpperCase()) {
            case "IMMEUBLES" -> {
                fillImmeublesData(data);
                List<?> immeubles = (List<?>) data.get("immeubles");
                data.put("empty", immeubles == null || immeubles.isEmpty());
            }
            case "UNITES" -> {
                fillUnitesData(data);
                List<?> unites = (List<?>) data.get("unites");
                data.put("empty", unites == null || unites.isEmpty());
            }
            case "UTILISATEURS" -> {
                fillUtilisateursData(data);
                List<?> utilisateurs = (List<?>) data.get("utilisateurs");
                data.put("empty", utilisateurs == null || utilisateurs.isEmpty());
            }
            case "CONTRATS" -> {
                fillContratsData(data);
                List<?> contrats = (List<?>) data.get("contrats");
                data.put("empty", contrats == null || contrats.isEmpty());
            }
            case "PAIEMENTS" -> {
                fillPaiementsData(data);
                List<?> paiements = (List<?>) data.get("paiements");
                data.put("empty", paiements == null || paiements.isEmpty());
            }
            default -> data.put("empty", true);
        }

        // 1. Préparation du contexte Thymeleaf
        Context context = new Context();
        context.setVariables(data);

        // 2. Rendu HTML
        String template = "rapports/report_" + type.toLowerCase() + ".html";
        String html = templateEngine.process(template, context);

        // 3. Génération du PDF
        String filename = "rapport_" + type.toLowerCase() + "_" + System.currentTimeMillis() + ".pdf";
        Path reportsPath = Path.of(REPORTS_DIR);
        if (!Files.exists(reportsPath)) Files.createDirectories(reportsPath);
        String filePath = reportsPath.resolve(filename).toString();
        try (FileOutputStream os = new FileOutputStream(filePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, new File(REPORTS_DIR).toURI().toString());
            builder.toStream(os);
            builder.run();
        }

        // 4. Sauvegarde en base
        Rapport rapport = Rapport.builder()
                .type(type)
                .filename(filename)
                .path(filePath)
                .generatedAt(LocalDateTime.now())
                .generatedBy(generatedBy)
                .build();
        return rapportRepository.save(rapport);
    }

    private void fillUnitesData(Map<String, Object> data) {
        var unites = uniteLocationRepository.findAll();
        var disponibles = unites.stream().filter(u -> u.getStatut() != null && u.getStatut().name().equals("DISPONIBLE")).count();
        data.put("unites", unites);
        data.put("totalUnites", unites.size());
        data.put("totalDisponibles", disponibles);
        // Pour le graphique : labels = statuts, data = nombre par statut
        List<String> uniteLabels = List.of("DISPONIBLE", "OCCUPE");
        List<Long> uniteData = uniteLabels.stream().map(l -> unites.stream().filter(u -> u.getStatut() != null && u.getStatut().name().equals(l)).count()).toList();
        data.put("uniteLabels", uniteLabels);
        data.put("uniteData", uniteData);
    }

    private void fillContratsData(Map<String, Object> data) {
        var contrats = contratRepository.findAll();
        var actifs = contrats.stream().filter(c -> c.getDateFin() != null && c.getDateFin().isAfter(java.time.LocalDate.now())).count();
        data.put("contrats", contrats);
        data.put("totalContrats", contrats.size());
        data.put("totalActifs", actifs);
        // Pour le graphique : labels = [Actifs, Terminés], data = nombre
        List<String> contratLabels = List.of("Actifs", "Terminés");
        List<Long> contratData = List.of(actifs, (long)contrats.size() - actifs);
        data.put("contratLabels", contratLabels);
        data.put("contratData", contratData);
    }

    private void fillPaiementsData(Map<String, Object> data) {
        var paiements = paiementRepository.findAll();
        var totalMontant = paiements.stream().map(p -> p.getMontant()).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        data.put("paiements", paiements);
        data.put("totalPaiements", paiements.size());
        data.put("totalMontant", totalMontant);
        // Pour le graphique : labels = mois, data = montant total par mois
        var moisMontants = new java.util.LinkedHashMap<String, java.math.BigDecimal>();
        for (var p : paiements) {
            String mois = p.getDatePaiement() != null ? p.getDatePaiement().getMonth().toString() + " " + p.getDatePaiement().getYear() : "Inconnu";
            moisMontants.putIfAbsent(mois, java.math.BigDecimal.ZERO);
            moisMontants.put(mois, moisMontants.get(mois).add(p.getMontant()));
        }
        data.put("paiementLabels", new java.util.ArrayList<>(moisMontants.keySet()));
        data.put("paiementData", moisMontants.values().stream().toList());
    }

    private void fillUtilisateursData(Map<String, Object> data) {
        var utilisateurs = utilisateurRepository.findAll();
        data.put("utilisateurs", utilisateurs);
        data.put("totalUtilisateurs", utilisateurs.size());
        // Pour le graphique : labels = première lettre du nom, data = nombre par lettre
        var map = new java.util.LinkedHashMap<String, Long>();
        for (var u : utilisateurs) {
            String lettre = u.getNom() != null && !u.getNom().isEmpty() ? u.getNom().substring(0,1).toUpperCase() : "?";
            map.put(lettre, map.getOrDefault(lettre, 0L) + 1);
        }
        data.put("userLabels", new java.util.ArrayList<>(map.keySet()));
        data.put("userData", map.values().stream().toList());
    }

    private void fillImmeublesData(Map<String, Object> data) {
        var immeubles = immeubleRepository.findAll();
        var unites = uniteLocationRepository.findAll();
        data.put("immeubles", immeubles);
        data.put("totalImmeubles", immeubles.size());
        data.put("totalUnites", unites.size());
        // Pour le graphique : labels = noms immeubles, data = nb unités par immeuble
        List<String> immeubleLabels = immeubles.stream().map(i -> i.getNom()).collect(Collectors.toList());
        List<Integer> immeubleData = immeubles.stream().map(i -> i.getNombreUnites()).collect(Collectors.toList());
        data.put("immeubleLabels", immeubleLabels);
        data.put("immeubleData", immeubleData);
    }

    public byte[] getPdfContent(Long id) throws Exception {
        Rapport rapport = rapportRepository.findById(id).orElseThrow();
        return Files.readAllBytes(Path.of(rapport.getPath()));
    }

    public String getPdfPath(Long id) {
        Rapport rapport = rapportRepository.findById(id).orElseThrow();
        return rapport.getPath();
    }
}
