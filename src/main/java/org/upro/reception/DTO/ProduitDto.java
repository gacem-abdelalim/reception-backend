package org.upro.reception.DTO;

public record ProduitDto(
        Integer medId,
        String produit,
        String labo,
        String dosage,
        String condit,
        String forme
) {}
