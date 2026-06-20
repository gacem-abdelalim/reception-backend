package org.upro.reception.Service;


import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.upro.reception.DTO.ProduitDto;
import org.upro.reception.LogiPharm.oracleReadRepository;

import java.util.List;

@Service
public class ProduitCacheService {

    private static final String CACHE_NAME = "produits";

    private final oracleReadRepository oracleReadRepository;

    public ProduitCacheService(oracleReadRepository oracleReadRepository) {
        this.oracleReadRepository = oracleReadRepository;
    }

    /**
     * First call loads data, then cached
     */
    @Cacheable(value = CACHE_NAME)
    public List<ProduitDto> getProduits() {
        System.out.println("Fetching produits from Oracle...");
        return oracleReadRepository.getProduits();
    }

    /**
     * Refresh cache every day
     * (24h = 24 * 60 * 60 * 1000 ms)
     */
    @CachePut(value = CACHE_NAME)
    @Scheduled(cron = "0 0 2 * * ?")
    public List<ProduitDto> refreshProduitsCache() {
        System.out.println("Refreshing produits cache...");
        return oracleReadRepository.getProduits();
    }
}