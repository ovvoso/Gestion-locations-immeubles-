package sn.diti4.gestionlocationimmeubles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "unites")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniteLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String numero;

    private double surface;

    private int pieces;

    @Column(nullable = false)
    private BigDecimal loyer;

    @Enumerated(EnumType.STRING)
    private StatutUnite statut; // DISPONIBLE ou OCCUPE

    @ManyToOne
    @JoinColumn(name = "immeuble_id")
    private Immeuble immeuble;

    @OneToMany(mappedBy = "unite", cascade = CascadeType.ALL)
    private List<Contrat> contrats;
}
