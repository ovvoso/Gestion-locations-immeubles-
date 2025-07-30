package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.Paiement;
import sn.diti4.gestionlocationimmeubles.entity.StatutPaiement;
import sn.diti4.gestionlocationimmeubles.repository.ContratRepository;
import sn.diti4.gestionlocationimmeubles.repository.PaiementRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/paiements")
public class PaiementController {

    private final PaiementRepository paiementRepository;
    private final ContratRepository contratRepository;

    // ✅ Liste des paiements
    @GetMapping
    public String listPaiements(Model model) {
        model.addAttribute("paiements", paiementRepository.findAll());
        return "paiements/list";
    }

    // ✅ Formulaire d'ajout
    @GetMapping("/ajouter")
    public String showForm(Model model) {
        model.addAttribute("paiement", new Paiement());
        model.addAttribute("contrats", contratRepository.findAll());
        model.addAttribute("statuts", StatutPaiement.values());
        return "paiements/form";
    }

    // ✅ Enregistrer
    @PostMapping("/enregistrer")
    public String savePaiement(
            @ModelAttribute("paiement") Paiement paiement,
            @RequestParam("contrat.id") Long contratId) {

        Contrat contrat = contratRepository.findById(contratId)
                .orElseThrow(() -> new IllegalArgumentException("Contrat introuvable : " + contratId));

        paiement.setContrat(contrat);
        paiementRepository.save(paiement);
        return "redirect:/paiements";
    }

    // ✅ Modifier
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paiement introuvable : " + id));

        model.addAttribute("paiement", paiement);
        model.addAttribute("contrats", contratRepository.findAll());
        model.addAttribute("statuts", StatutPaiement.values());
        return "paiements/form";
    }

    // ✅ Supprimer
    @GetMapping("/supprimer/{id}")
    public String deletePaiement(@PathVariable Long id) {
        paiementRepository.deleteById(id);
        return "redirect:/paiements";
    }
}
