package sn.diti4.gestionlocationimmeubles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diti4.gestionlocationimmeubles.entity.UniteLocation;
import sn.diti4.gestionlocationimmeubles.entity.StatutUnite;
import sn.diti4.gestionlocationimmeubles.entity.Immeuble;
import sn.diti4.gestionlocationimmeubles.repository.UniteLocationRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UniteLocationService {

    private final UniteLocationRepository uniteLocationRepository;

    public List<UniteLocation> findAll() {
        return uniteLocationRepository.findAll();
    }

    public Optional<UniteLocation> findById(Long id) {
        return uniteLocationRepository.findById(id);
    }

    public UniteLocation save(UniteLocation unite) {
        return uniteLocationRepository.save(unite);
    }

    public void deleteById(Long id) {
        if (!uniteLocationRepository.existsById(id)) {
            throw new RuntimeException("Unité introuvable avec l'ID : " + id);
        }
        uniteLocationRepository.deleteById(id);
    }

    public List<UniteLocation> findByStatut(StatutUnite statut) {
        return uniteLocationRepository.findByStatut(statut);
    }

    public List<UniteLocation> findByImmeuble(Immeuble immeuble) {
        return uniteLocationRepository.findByImmeuble(immeuble);
    }

    public List<UniteLocation> findAvailableUnitsWithFilters(Integer piecesMin, Integer piecesMax, 
                                                            BigDecimal loyerMin, BigDecimal loyerMax) {
        return uniteLocationRepository.findAvailableUnitsWithFilters(
            StatutUnite.DISPONIBLE, piecesMin, piecesMax, loyerMin, loyerMax);
    }

    public long count() {
        return uniteLocationRepository.count();
    }

    public void marquerCommeOccupe(Long uniteId) {
        UniteLocation unite = findById(uniteId)
            .orElseThrow(() -> new RuntimeException("Unité introuvable avec l'ID : " + uniteId));
        unite.setStatut(StatutUnite.OCCUPE);
        save(unite);
    }

    public void marquerCommeDisponible(Long uniteId) {
        UniteLocation unite = findById(uniteId)
            .orElseThrow(() -> new RuntimeException("Unité introuvable avec l'ID : " + uniteId));
        unite.setStatut(StatutUnite.DISPONIBLE);
        save(unite);
    }
}
