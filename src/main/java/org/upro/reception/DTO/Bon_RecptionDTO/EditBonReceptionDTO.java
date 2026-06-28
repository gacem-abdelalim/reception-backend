package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.LocalDate;
import java.util.List;

public record EditBonReceptionDTO(
        LocalDate dateReception,
        Integer fourId,
        String fourName,
        List<ReceptionFactureDTO> factures
) {}