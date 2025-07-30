package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.diti4.gestionlocationimmeubles.entity.Immeuble;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.repository.ImmeubleRepository;
import sn.diti4.gestionlocationimmeubles.repository.UtilisateurRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/immeubles")
public class ImmeubleController {

    private final ImmeubleRepository immeubleRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ✅ Afficher la liste
    @GetMapping
    public String listImmeubles(Model model) {
        model.addAttribute("immeubles", immeubleRepository.findAll());
        return "immeubles/list";
    }

    // ✅ Afficher le formulaire d'ajout
    @GetMapping("/ajouter")
    public String showForm(Model model) {
        model.addAttribute("immeuble", new Immeuble());
        model.addAttribute("proprietaires", utilisateurRepository.findAll()); // choix propriétaire
        return "immeubles/form";
    }

    // ✅ Enregistrer un immeuble
    @PostMapping("/enregistrer")
    public String saveImmeuble(@ModelAttribute("immeuble") Immeuble immeuble, @RequestParam("proprietaire.id") Long proprietaireId) {
        Utilisateur proprietaire = utilisateurRepository.findById(proprietaireId)
                .orElseThrow(() -> new IllegalArgumentException("Propriétaire introuvable : " + proprietaireId));
        immeuble.setProprietaire(proprietaire);
        immeubleRepository.save(immeuble);
        return "redirect:/immeubles";
    }


    // ✅ Afficher formulaire de modification
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Immeuble immeuble = immeubleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Immeuble introuvable : " + id));
        model.addAttribute("immeuble", immeuble);
        model.addAttribute("proprietaires", utilisateurRepository.findAll());
        return "immeubles/form";
    }

    // ✅ Supprimer un immeuble
    @GetMapping("/supprimer/{id}")
    public String deleteImmeuble(@PathVariable Long id) {
        immeubleRepository.deleteById(id);
        return "redirect:/immeubles";
    }
}
