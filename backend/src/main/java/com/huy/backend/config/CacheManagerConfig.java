package com.huy.backend.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@Configurable
public class CacheManagerConfig {

    private static final String MOVIES_CACHE = "moviesCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(MOVIES_CACHE);
    }



}
