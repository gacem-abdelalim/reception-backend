package org.upro.reception.Service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.upro.reception.DTO.FournisseurDto;
import org.upro.reception.LogiPharm.oracleReadRepository;

import java.util.List;

@Service
public class FournisseurCacheService {

    private static final String CACHE_NAME = "fournisseurs";

    private final oracleReadRepository oracleReadRepository;

    public FournisseurCacheService(oracleReadRepository oracleReadRepository) {
        this.oracleReadRepository = oracleReadRepository;
    }

    /**
     * First call loads data, then cached
     */
    @Cacheable(value = CACHE_NAME)
    public List<FournisseurDto> getFournisseurs() {
        System.out.println("Fetching fournisseurs from Oracle...");
        return oracleReadRepository.getFournisseurs();
    }

    /**
     * Refresh cache every week
     * (7 days = 7 * 24 * 60 * 60 * 1000 ms)
     */
    @CachePut(value = CACHE_NAME)
    @Scheduled(cron = "0 0 2 ? * SUN")
    public List<FournisseurDto> refreshFournisseursCache() {
        System.out.println("Refreshing fournisseurs cache...");
        return oracleReadRepository.getFournisseurs();
    }
}