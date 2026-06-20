package org.upro.reception.DTO;


import org.upro.reception.DTO.Bon_RecptionDTO.ReceptionFactureDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

// Response DTO
public record BonReceptionResponseDTO(
        Integer id,
        LocalDate dateReception,
        Integer fourId,
        String fourName,
        String createdBy,
        Instant createdAt,

        Boolean isValidated,
        Instant validatedAt,
        String validatedBy,

        List<ReceptionFactureDTO> factures,
        List<LigneBonResponseDTO> lignes
) {}