package sn.diti4.gestionlocationimmeubles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import sn.diti4.gestionlocationimmeubles.entity.UniteLocation;
import java.util.List;

@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {
    List<Contrat> findByLocataire(Utilisateur locataire);
    List<Contrat> findByUnite(UniteLocation unite);
    boolean existsByUniteAndDateFinAfter(UniteLocation unite, java.time.LocalDate date);
}
