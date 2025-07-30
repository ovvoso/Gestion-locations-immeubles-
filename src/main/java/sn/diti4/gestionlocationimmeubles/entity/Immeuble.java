package sn.diti4.gestionlocationimmeubles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "immeubles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Immeuble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Column(length = 500)
    private String description;

    private String equipements;

    private int nombreUnites;

    // Relation avec utilisateur (propriétaire)
    @ManyToOne
    @JoinColumn(name = "proprietaire_id")
    private Utilisateur proprietaire;

    // Relation avec unités
    @OneToMany(mappedBy = "immeuble", cascade = CascadeType.ALL)
    private List<UniteLocation> unites;
}
