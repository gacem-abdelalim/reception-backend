package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.Instant;
import java.time.LocalDate;

public record ReceptionFactureDTO(
        LocalDate date,
        String ref,
        Instant createdAt
) {};
