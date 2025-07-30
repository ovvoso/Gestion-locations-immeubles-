package sn.diti4.gestionlocationimmeubles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sn.diti4.gestionlocationimmeubles.repository.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UtilisateurRepository utilisateurRepository;
    private final ImmeubleRepository immeubleRepository;
    private final UniteLocationRepository uniteLocationRepository;
    private final PaiementRepository paiementRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalUtilisateurs", utilisateurRepository.count());
        model.addAttribute("totalImmeubles", immeubleRepository.count());
        model.addAttribute("totalUnites", uniteLocationRepository.count());
        model.addAttribute("totalPaiements", paiementRepository.count());

        return "index";
    }
}
