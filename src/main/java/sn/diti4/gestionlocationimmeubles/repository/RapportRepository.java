package sn.diti4.gestionlocationimmeubles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.diti4.gestionlocationimmeubles.entity.Rapport;
import java.util.List;

public interface RapportRepository extends JpaRepository<Rapport, Long> {
    List<Rapport> findByTypeOrderByGeneratedAtDesc(String type);
    List<Rapport> findAllByOrderByGeneratedAtDesc();
}
