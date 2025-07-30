package sn.diti4.gestionlocationimmeubles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.diti4.gestionlocationimmeubles.entity.Paiement;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.StatutPaiement;
import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByContrat(Contrat contrat);
    List<Paiement> findByStatut(StatutPaiement statut);
    List<Paiement> findByContratOrderByDatePaiementDesc(Contrat contrat);
}
