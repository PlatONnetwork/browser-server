package com.platon.browser.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * caffeine缓存配置
 *
 * @date 2021/5/15
 */
@Configuration
public class CaffeineConfig {

    /**
     * caffeine缓存管理
     */
    @Bean
    public CacheManager caffeineCacheManager() {
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.SECONDS);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

}
