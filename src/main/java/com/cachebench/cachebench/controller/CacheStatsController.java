package com.cachebench.cachebench.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class CacheStatsController {

    private final CacheManager cacheManager;

    @GetMapping("/cache-stats")
    public Map<String, Object> getCacheStats() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("cacheManagerType", cacheManager.getClass().getSimpleName());

        Map<String, Object> caches = new LinkedHashMap<>();
        long totalHits = 0;
        long totalMisses = 0;

        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                        caffeineCache.getNativeCache();
                CacheStats stats = nativeCache.stats();

                Map<String, Object> cacheData = new LinkedHashMap<>();
                cacheData.put("size", nativeCache.estimatedSize());
                cacheData.put("hitCount", stats.hitCount());
                cacheData.put("missCount", stats.missCount());
                cacheData.put("requestCount", stats.requestCount());
                cacheData.put("hitRatio", String.format("%.2f %%", stats.hitRate() * 100));
                cacheData.put("missRatio", String.format("%.2f %%", stats.missRate() * 100));
                cacheData.put("evictionCount", stats.evictionCount());
                cacheData.put("averageLoadTimeMs",
                        String.format("%.3f", stats.averageLoadPenalty() / 1_000_000.0));

                totalHits += stats.hitCount();
                totalMisses += stats.missCount();
                caches.put(cacheName, cacheData);
            } else if (cache != null) {
                caches.put(cacheName, "Not a Caffeine cache (no stats available)");
            }
        }

        // Resume global
        Map<String, Object> summary = new LinkedHashMap<>();
        long totalRequests = totalHits + totalMisses;
        summary.put("totalHits", totalHits);
        summary.put("totalMisses", totalMisses);
        summary.put("totalRequests", totalRequests);
        summary.put("globalHitRatio", totalRequests == 0
                ? "N/A"
                : String.format("%.2f %%", (double) totalHits / totalRequests * 100));

        result.put("summary", summary);
        result.put("caches", caches);
        return result;
    }

    @PostMapping("/cache-clear")
    public Map<String, String> clearAllCaches() {
        cacheManager.getCacheNames().forEach(name -> {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        });
        return Map.of("status", "All caches cleared");
    }
}