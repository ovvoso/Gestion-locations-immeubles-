package sn.diti4.gestionlocationimmeubles.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate datePaiement;

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut; // PAYE, EN_RETARD, NON_PAYE

    @ManyToOne
    @JoinColumn(name = "contrat_id")
    private Contrat contrat;
}

