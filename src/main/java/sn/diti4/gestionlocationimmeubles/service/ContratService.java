package sn.diti4.gestionlocationimmeubles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.entity.UniteLocation;
import sn.diti4.gestionlocationimmeubles.entity.StatutUnite;
import sn.diti4.gestionlocationimmeubles.repository.ContratRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContratService {

    private final ContratRepository contratRepository;
    private final UniteLocationService uniteLocationService;

    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    public Optional<Contrat> findById(Long id) {
        return contratRepository.findById(id);
    }

    public Contrat save(Contrat contrat) {
        // Validation des dates
        if (contrat.getDateFin().isBefore(contrat.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être après la date de début");
        }

        // Vérifier si l'unité est disponible
        if (contrat.getId() == null) { // Nouveau contrat
            boolean uniteOccupee = contratRepository.existsByUniteAndDateFinAfter(
                contrat.getUnite(), LocalDate.now());
            
            if (uniteOccupee) {
                throw new RuntimeException("Cette unité est déjà louée pour cette période");
            }

            // Marquer l'unité comme occupée
            uniteLocationService.marquerCommeOccupe(contrat.getUnite().getId());
        }

        return contratRepository.save(contrat);
    }

    public void deleteById(Long id) {
        Contrat contrat = findById(id)
            .orElseThrow(() -> new RuntimeException("Contrat introuvable avec l'ID : " + id));
        
        // Libérer l'unité
        uniteLocationService.marquerCommeDisponible(contrat.getUnite().getId());
        
        contratRepository.deleteById(id);
    }

    public List<Contrat> findByLocataire(Utilisateur locataire) {
        return contratRepository.findByLocataire(locataire);
    }

    public List<Contrat> findByUnite(UniteLocation unite) {
        return contratRepository.findByUnite(unite);
    }

    public long count() {
        return contratRepository.count();
    }

    public List<Contrat> findActiveContracts() {
        return contratRepository.findAll().stream()
            .filter(c -> c.getDateFin().isAfter(LocalDate.now()))
            .toList();
    }
}
