package com.esm.faceitstats.service;

import com.esm.faceitstats.component.SynchronizedCacheManager;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenCacheService {

    private SynchronizedCacheManager<String, String> cacheManager;

    @Autowired
    public void setCacheManager(SynchronizedCacheManager<String, String> cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void blacklistToken(String token, long ttlMillis) {
        cacheManager.put(token, "blacklisted", ttlMillis);
    }

    public boolean isTokenBlacklisted(String token) {
        return cacheManager.get(token) != null;
    }

    public void evictToken(String token) {
        cacheManager.evict(token);
    }
}

