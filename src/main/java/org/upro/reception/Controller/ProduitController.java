package org.upro.reception.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upro.reception.DTO.ProduitDto;
import org.upro.reception.Service.ProduitCacheService;

import java.util.List;

@RestController
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitCacheService produitCacheService;

    public ProduitController(ProduitCacheService produitCacheService) {
        this.produitCacheService = produitCacheService;
    }

    @GetMapping
    public ResponseEntity<List<ProduitDto>> getProduits() {
        return ResponseEntity.ok(produitCacheService.getProduits());
    }

    @PutMapping("/refresh")
    public ResponseEntity<List<ProduitDto>> refreshProduits() {
        return ResponseEntity.ok(produitCacheService.refreshProduitsCache());
    }
}