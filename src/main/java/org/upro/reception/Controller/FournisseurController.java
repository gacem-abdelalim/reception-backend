package org.upro.reception.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upro.reception.DTO.FournisseurDto;
import org.upro.reception.Service.FournisseurCacheService;

import java.util.List;

@RestController
@RequestMapping("/fournisseurs")
public class FournisseurController {

    private final FournisseurCacheService fournisseurCacheService;

    public FournisseurController(FournisseurCacheService fournisseurCacheService) {
        this.fournisseurCacheService = fournisseurCacheService;
    }

    @GetMapping
    public ResponseEntity<List<FournisseurDto>> getFournisseurs() {
        return ResponseEntity.ok(fournisseurCacheService.getFournisseurs());
    }

    @PutMapping("/refresh")
    public ResponseEntity<List<FournisseurDto>> refreshFournisseurs() {
        return ResponseEntity.ok(fournisseurCacheService.refreshFournisseursCache());
    }
}