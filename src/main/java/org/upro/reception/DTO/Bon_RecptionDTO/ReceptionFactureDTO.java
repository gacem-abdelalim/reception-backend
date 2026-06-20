package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.Instant;
import java.time.LocalDate;

public record ReceptionFactureDTO(
        LocalDate dateFacture,
        String ref,
        Instant createdAt
) {};
