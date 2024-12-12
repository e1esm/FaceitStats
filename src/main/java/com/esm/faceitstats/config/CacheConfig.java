package com.esm.faceitstats.config;

import com.esm.faceitstats.component.SynchronizedCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public SynchronizedCacheManager<String, String> synchronizedCacheManager() {
        return new SynchronizedCacheManager<>();
    }
}
