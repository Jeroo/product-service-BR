package com.banreservas.product.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CacheMetrics {

    private final Counter cacheHits;
    private final Counter cacheMisses;

    @Inject
    public CacheMetrics(MeterRegistry registry) {
        this.cacheHits = registry.counter("cache.hits");
        this.cacheMisses = registry.counter("cache.misses");
    }

    public void incrementCacheHit() {
        cacheHits.increment();
    }

    public void incrementCacheMiss() {
        cacheMisses.increment();
    }
}
