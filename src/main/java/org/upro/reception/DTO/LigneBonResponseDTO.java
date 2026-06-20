package org.upro.reception.DTO;


import java.time.Instant;
import java.time.LocalDate;

public record LigneBonResponseDTO(
        Integer id,
        Integer medId,
        String lot,
        Integer qnt,
        Integer colis,
        Integer vrag,
        Integer qteAbime,
        String name,
        String dosage,
        String forme,
        LocalDate ddp,
        LocalDate ddf,
        Double ppa,
        Double shp,
        Integer colissage,
        String createdBy,
        Instant createdAt
) {}