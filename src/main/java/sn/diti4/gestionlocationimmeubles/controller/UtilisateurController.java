package sn.diti4.gestionlocationimmeubles.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.service.UtilisateurService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    // ✅ Afficher la liste des utilisateurs
    @GetMapping
    public String listUtilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurService.findAll());
        return "utilisateurs/list";
    }

    // ✅ Afficher le formulaire d'ajout
    @GetMapping("/ajouter")
    public String showForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "utilisateurs/form";
    }

    // ✅ Enregistrer l'utilisateur
    @PostMapping("/enregistrer")
    public String saveUtilisateur(@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "utilisateurs/form";
        }

        try {
            utilisateurService.save(utilisateur);
            redirectAttributes.addFlashAttribute("success", "Utilisateur enregistré avec succès!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/utilisateurs";
    }

    // ✅ Supprimer un utilisateur
    @GetMapping("/supprimer/{id}")
    public String deleteUtilisateur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur supprimé avec succès!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/utilisateurs";
    }

    // ✅ Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Utilisateur utilisateur = utilisateurService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable : " + id));
        model.addAttribute("utilisateur", utilisateur);
        return "utilisateurs/form";
    }

}
