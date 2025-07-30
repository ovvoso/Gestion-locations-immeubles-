package sn.diti4.gestionlocationimmeubles.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.diti4.gestionlocationimmeubles.entity.Paiement;
import sn.diti4.gestionlocationimmeubles.entity.Contrat;
import sn.diti4.gestionlocationimmeubles.entity.StatutPaiement;
import sn.diti4.gestionlocationimmeubles.repository.PaiementRepository;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;

    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> findById(Long id) {
        return paiementRepository.findById(id);
    }

    public Paiement save(Paiement paiement) {
        // Auto-définir la date si non spécifiée
        if (paiement.getDatePaiement() == null) {
            paiement.setDatePaiement(LocalDate.now());
        }

        // Auto-définir le statut si non spécifié
        if (paiement.getStatut() == null) {
            paiement.setStatut(StatutPaiement.PAYE);
        }

        return paiementRepository.save(paiement);
    }

    public void deleteById(Long id) {
        if (!paiementRepository.existsById(id)) {
            throw new RuntimeException("Paiement introuvable avec l'ID : " + id);
        }
        paiementRepository.deleteById(id);
    }

    public List<Paiement> findByContrat(Contrat contrat) {
        return paiementRepository.findByContratOrderByDatePaiementDesc(contrat);
    }

    public List<Paiement> findByStatut(StatutPaiement statut) {
        return paiementRepository.findByStatut(statut);
    }

    public long count() {
        return paiementRepository.count();
    }

    public List<Paiement> findPaiementsEnRetard() {
        return paiementRepository.findByStatut(StatutPaiement.EN_RETARD);
    }

    public BigDecimal calculerTotalPaiements(Contrat contrat) {
        return findByContrat(contrat).stream()
            .filter(p -> p.getStatut() == StatutPaiement.PAYE)
            .map(Paiement::getMontant)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void marquerCommeEnRetard(Long paiementId) {
        Paiement paiement = findById(paiementId)
            .orElseThrow(() -> new RuntimeException("Paiement introuvable avec l'ID : " + paiementId));
        paiement.setStatut(StatutPaiement.EN_RETARD);
        save(paiement);
    }

    public void marquerCommePaye(Long paiementId) {
        Paiement paiement = findById(paiementId)
            .orElseThrow(() -> new RuntimeException("Paiement introuvable avec l'ID : " + paiementId));
        paiement.setStatut(StatutPaiement.PAYE);
        paiement.setDatePaiement(LocalDate.now());
        save(paiement);
    }
}
