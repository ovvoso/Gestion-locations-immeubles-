package sn.diti4.gestionlocationimmeubles.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.diti4.gestionlocationimmeubles.entity.UniteLocation;
import sn.diti4.gestionlocationimmeubles.entity.StatutUnite;
import sn.diti4.gestionlocationimmeubles.entity.Immeuble;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UniteLocationRepository extends JpaRepository<UniteLocation, Long> {
    List<UniteLocation> findByStatut(StatutUnite statut);
    List<UniteLocation> findByImmeuble(Immeuble immeuble);
    
    @Query("SELECT u FROM UniteLocation u WHERE u.statut = :statut " +
           "AND (:piecesMin IS NULL OR u.pieces >= :piecesMin) " +
           "AND (:piecesMax IS NULL OR u.pieces <= :piecesMax) " +
           "AND (:loyerMin IS NULL OR u.loyer >= :loyerMin) " +
           "AND (:loyerMax IS NULL OR u.loyer <= :loyerMax)")
    List<UniteLocation> findAvailableUnitsWithFilters(
        @Param("statut") StatutUnite statut,
        @Param("piecesMin") Integer piecesMin,
        @Param("piecesMax") Integer piecesMax,
        @Param("loyerMin") BigDecimal loyerMin,
        @Param("loyerMax") BigDecimal loyerMax
    );
}
