package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.LocalDate;

public record EditLigneBonDTO(
        String lot,
        Integer qte,
        Integer colis,
        Integer vrag,
        Integer qteAbime,
        String name,
        String labo,
        String dosage,
        String forme,
        LocalDate ddp,
        LocalDate ddf,
        double ppa,
        double shp,
        Integer colissage
) {}