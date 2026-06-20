package org.upro.reception.DTO.Bon_RecptionDTO;

import java.time.LocalDate;

public record CreateLigneBonDTO(

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

         Integer colissage
) {}