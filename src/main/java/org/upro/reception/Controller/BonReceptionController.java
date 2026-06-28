package org.upro.reception.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.upro.reception.DTO.BonReceptionResponseDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.CreateBonReceptionResponseDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.CreateLigneBonDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.EditBonReceptionDTO;
import org.upro.reception.DTO.Bon_RecptionDTO.EditLigneBonDTO;
import org.upro.reception.DTO.FournisseurDto;
import org.upro.reception.DTO.LigneBonResponseDTO;
import org.upro.reception.DTO.ProduitDto;
import org.upro.reception.Service.BonReceptionService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/bon-reception")
@RequiredArgsConstructor
public class BonReceptionController {

    private final BonReceptionService service;


    private final CacheManager cacheManager;


    // =========================
    // CREATE BON
    // =========================
    @PostMapping
    public Integer createBon(@RequestBody CreateBonReceptionResponseDTO dto) {

        return service.createBonReception(dto);
    }

    // =========================
    // ADD LIGNE
    // =========================
    @PostMapping("/{brcId}/ligne")
    public Integer addLigne(
            @PathVariable Integer brcId,
            @RequestBody CreateLigneBonDTO dto
    ) {
        return service.addLigneBon(brcId, dto);
    }



    // =========================
    // GET ALL
    // =========================
    @GetMapping
    public List<BonReceptionResponseDTO> getAll() {
        return service.getAll();
    }

    // =========================
    // GET TODAY
    // =========================
    @GetMapping("/today")
    public List<BonReceptionResponseDTO> getToday() {
        return service.getToday();
    }

    // =========================
    // GET BETWEEN DATES
    // =========================
    @GetMapping("/between")
    public List<BonReceptionResponseDTO> getBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
    ) {
        return service.getBetween(start, end);
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<BonReceptionResponseDTO> validate(@PathVariable Integer id) {
        return ResponseEntity.ok(service.validateBon(id));
    }

    @PutMapping("/{id}/cloture")
    public ResponseEntity<BonReceptionResponseDTO> cloture(@PathVariable Integer id) {
        return ResponseEntity.ok(service.clotureBon(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BonReceptionResponseDTO> editBonReception(
            @PathVariable Integer id,
            @RequestBody EditBonReceptionDTO dto
    ) {
        return ResponseEntity.ok(service.editBonReception(id, dto));
    }

    @PutMapping("/ligne/{ligneId}")
    public ResponseEntity<LigneBonResponseDTO> editLigneBon(
            @PathVariable Integer ligneId,
            @RequestBody EditLigneBonDTO dto
    ) {
        return ResponseEntity.ok(service.editLigneBon(ligneId, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBonReception(@PathVariable Integer id) {
        service.deleteBonReception(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ligne/{ligneId}")
    public ResponseEntity<Void> deleteLigneBon(@PathVariable Integer ligneId) {
        service.deleteLigneBon(ligneId);
        return ResponseEntity.noContent().build();
    }


    /**
     * Fetch fournisseurs from cache
     */
    @GetMapping("/fournisseurs")
    public ResponseEntity<List<FournisseurDto>> getFournisseurs() {

        var cache = cacheManager.getCache("fournisseurs");
        if (cache == null) return ResponseEntity.ok(Collections.emptyList());

        var cached = cache.get(SimpleKey.EMPTY, List.class);
        if (cached == null) return ResponseEntity.ok(Collections.emptyList());

        @SuppressWarnings("unchecked")
        List<FournisseurDto> data = (List<FournisseurDto>) cached;

        return ResponseEntity.ok(data);
    }

    /**
     * Fetch produits from cache
     */
    @GetMapping("/produits")
    public ResponseEntity<List<ProduitDto>> getProduits() {

        var cache = cacheManager.getCache("produits");
        if (cache == null) return ResponseEntity.ok(Collections.emptyList());

        var cached = cache.get(SimpleKey.EMPTY, List.class);
        if (cached == null) return ResponseEntity.ok(Collections.emptyList());

        @SuppressWarnings("unchecked")
        List<ProduitDto> data = (List<ProduitDto>) cached;

        return ResponseEntity.ok(data);
    }





}