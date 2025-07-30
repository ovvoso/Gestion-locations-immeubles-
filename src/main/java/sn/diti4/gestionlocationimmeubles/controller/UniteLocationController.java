package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sn.diti4.gestionlocationimmeubles.entity.*;
import sn.diti4.gestionlocationimmeubles.repository.ImmeubleRepository;
import sn.diti4.gestionlocationimmeubles.repository.UniteLocationRepository;

@Controller
@RequiredArgsConstructor
@RequestMapping("/unites")
public class UniteLocationController {

    private final UniteLocationRepository uniteLocationRepository;
    private final ImmeubleRepository immeubleRepository;

    // ✅ Liste des unités
    @GetMapping
    public String listUnites(Model model) {
        model.addAttribute("unites", uniteLocationRepository.findAll());
        return "unites/list";
    }

    // ✅ Formulaire d'ajout
    @GetMapping("/ajouter")
    public String showForm(Model model) {
        model.addAttribute("unite", new UniteLocation());
        model.addAttribute("immeubles", immeubleRepository.findAll());
        model.addAttribute("statuts", StatutUnite.values());
        return "unites/form";
    }

    // ✅ Enregistrer
    @PostMapping("/enregistrer")
    public String saveUnite(
            @ModelAttribute("unite") UniteLocation unite,
            @RequestParam("immeuble.id") Long immeubleId) {

        Immeuble immeuble = immeubleRepository.findById(immeubleId)
                .orElseThrow(() -> new IllegalArgumentException("Immeuble introuvable : " + immeubleId));
        unite.setImmeuble(immeuble);

        uniteLocationRepository.save(unite);
        return "redirect:/unites";
    }

    // ✅ Modifier
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UniteLocation unite = uniteLocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unité introuvable : " + id));

        model.addAttribute("unite", unite);
        model.addAttribute("immeubles", immeubleRepository.findAll());
        model.addAttribute("statuts", StatutUnite.values());
        return "unites/form";
    }

    // ✅ Supprimer
    @GetMapping("/supprimer/{id}")
    public String deleteUnite(@PathVariable Long id) {
        uniteLocationRepository.deleteById(id);
        return "redirect:/unites";
    }
}
