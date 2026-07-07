package org.upro.reception.DTO;


import lombok.Getter;
import org.upro.reception.DTO.Bon_RecptionDTO.ReceptionFactureDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

// Res
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

        Boolean isCloture,
        Instant clotureAt,
        String clotureBy,


        List<ReceptionFactureDTO> factures,
        List<LigneBonResponseDTO> lignes
) {}