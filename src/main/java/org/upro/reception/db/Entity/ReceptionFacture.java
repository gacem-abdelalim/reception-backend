package org.upro.reception.db.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reception_facture")
public class ReceptionFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facture_id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "ref", nullable = false, length = 100)
    private String ref;

    @NotNull
    @Column(name = "date_facture", nullable = false)
    private LocalDate dateFacture;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brcp_id")
    private BonReception brcp;


}