package org.upro.reception.db.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "bon_reception")
public class BonReception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brcp_id", nullable = false)
    private Integer id;



    @Column(name = "date_reception")
    private LocalDate dateReception;

    @Column(name = "four_id")
    private Integer fourId;

    @Size(max = 255)
    @Column(name = "four_name")
    private String fourName;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("false")
    @Column(name = "is_validated")
    private Boolean isValidated;

    @Column(name = "validated_at")
    private Instant validatedAt;

    @Size(max = 100)
    @Column(name = "validated_by", length = 100)
    private String validatedBy;
    @OneToMany(mappedBy = "brcp")
    private Set<ReceptionFacture> receptionFactures = new LinkedHashSet<>();


}