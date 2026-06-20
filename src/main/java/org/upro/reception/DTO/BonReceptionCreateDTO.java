package org.upro.reception.DTO;
import java.time.LocalDate;

// Create Bon
public record BonReceptionCreateDTO(
        String refFacture,
        LocalDate dateFacture,
        LocalDate dateReception,
        Integer fourId,
        String fourName
) {}