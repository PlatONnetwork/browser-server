package com.platon.browser.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * caffeine缓存配置
 *
 * @date 2021/5/15
 */
@Configuration
public class CaffeineConfig {

    /**
     * caffeine缓存管理 (60s)
     */
    @Bean
    public CacheManager caffeineCacheManagerOf60() {
        return create(50, 500, 60);
    }

    /**
     * caffeine缓存管理 (5s)
     */
    @Bean
    public CacheManager caffeineCacheManagerOf5() {
        return create(100, 1000, 5);
    }

    /**
     * caffeine缓存管理 (1s)
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManagerOf1() {
        return create(100, 1000, 1);
    }

    private CacheManager create(int initialCapacity, int maximumSize, int expireAfterWriteSeconds){
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWriteSeconds, TimeUnit.SECONDS);
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
