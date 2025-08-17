package sn.diti4.gestionlocationimmeubles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // IMMEUBLES, UNITES, CONTRATS, PAIEMENTS, UTILISATEURS

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    private String generatedBy;
}
