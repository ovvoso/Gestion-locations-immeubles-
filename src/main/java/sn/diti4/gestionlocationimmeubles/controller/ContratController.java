package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.UniteLocation;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.repository.ContratRepository;
import sn.diti4.gestionlocationimmeubles.repository.UniteLocationRepository;
import sn.diti4.gestionlocationimmeubles.repository.UtilisateurRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contrats")
public class ContratController {

    private final ContratRepository contratRepository;
    private final UniteLocationRepository uniteLocationRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ✅ Liste des contrats
    @GetMapping
    public String listContrats(Model model) {
        model.addAttribute("contrats", contratRepository.findAll());
        return "contrats/list";
    }

    // ✅ Formulaire d'ajout
    @GetMapping("/ajouter")
    public String showForm(Model model) {
        model.addAttribute("contrat", new Contrat());
        model.addAttribute("unites", uniteLocationRepository.findAll());
        model.addAttribute("locataires", utilisateurRepository.findAll());
        return "contrats/form";
    }

    // ✅ Enregistrer
    @PostMapping("/enregistrer")
    public String saveContrat(
            @ModelAttribute("contrat") Contrat contrat,
            @RequestParam("locataire.id") Long locataireId,
            @RequestParam("unite.id") Long uniteId) {

        Utilisateur locataire = utilisateurRepository.findById(locataireId)
                .orElseThrow(() -> new IllegalArgumentException("Locataire introuvable : " + locataireId));

        UniteLocation unite = uniteLocationRepository.findById(uniteId)
                .orElseThrow(() -> new IllegalArgumentException("Unité introuvable : " + uniteId));

        contrat.setLocataire(locataire);
        contrat.setUnite(unite);

        contratRepository.save(contrat);
        return "redirect:/contrats";
    }

    // ✅ Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Contrat contrat = contratRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Contrat introuvable : " + id));

        model.addAttribute("contrat", contrat);
        model.addAttribute("unites", uniteLocationRepository.findAll());
        model.addAttribute("locataires", utilisateurRepository.findAll());
        return "contrats/form";
    }

    // ✅ Supprimer
    @GetMapping("/supprimer/{id}")
    public String deleteContrat(@PathVariable Long id) {
        contratRepository.deleteById(id);
        return "redirect:/contrats";
    }
}
