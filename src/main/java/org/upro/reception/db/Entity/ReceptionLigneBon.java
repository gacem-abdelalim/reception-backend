package org.upro.reception.db.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reception_ligne_bon")
public class ReceptionLigneBon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "brcp_id", nullable = false)
    private BonReception brcp;

    @NotNull
    @Column(name = "med_id", nullable = false)
    private Integer medId;


    @Size(max = 100)
    @Column(name = "lot", length = 100)
    private String lot;

    @NotNull
    @Column(name = "qnt", nullable = false)
    private Integer qte;


    @Column(name = "colis")
    private Integer colis;

    @Column(name = "vrag")
    private Integer vrag;

    @Column(name = "qte_abime")
    private Integer qteAbime;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Size(max = 255)
    @Column(name = "dosage")
    private String dosage;

    @Size(max = 100)
    @Column(name = "forme", length = 100)
    private String forme;

    @Column(name = "ddp")
    private LocalDate ddp;

    @Column(name = "ddf")
    private LocalDate ddf;

    @Column(name = "ppa")
    private Double ppa;

    @Column(name = "shp")
    private Double shp;

    @Column(name = "colissage")
    private Integer colissage;


    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 255)
    @Column(name = "labo")
    private String labo;


}