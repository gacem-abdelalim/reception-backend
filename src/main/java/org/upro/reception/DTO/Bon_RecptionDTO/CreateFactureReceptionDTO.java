package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.LocalDate;

public record CreateFactureReceptionDTO(
        String ref,
        LocalDate date

) {};
