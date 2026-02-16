package com.example.Blogging_platform2.controller;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheStatsController {

    private final CacheManager cacheManager;

    public CacheStatsController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping("/cache-stats/reviews")
    public String getReviewCacheStats() {
        Cache cache = cacheManager.getCache("reviews");
        if (cache instanceof CaffeineCache caffeineCache) {
            return caffeineCache.getNativeCache().stats().toString();
        }
        return "No stats available";
    }
}
