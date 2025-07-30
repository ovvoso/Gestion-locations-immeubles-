package sn.diti4.gestionlocationimmeubles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.diti4.gestionlocationimmeubles.entity.Immeuble;
import sn.diti4.gestionlocationimmeubles.entity.Utilisateur;
import java.util.List;

@Repository
public interface ImmeubleRepository extends JpaRepository<Immeuble, Long> {
    List<Immeuble> findByProprietaire(Utilisateur proprietaire);
    List<Immeuble> findByAdresseContainingIgnoreCase(String adresse);
}
