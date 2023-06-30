package com.platon.browser.test;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;

@Slf4j
public class NewAddressCacheTest {



    /**
     * 缓存范围：进程
     */
    private LoadingCache<String, String> AllAddressCache = Caffeine.newBuilder().maximumSize(10).build(new CacheLoader<String, String>() {
        @Override
        public @Nullable String load(@NonNull String s) {
            if (s.equalsIgnoreCase("test")){
                return "haha";
            }else{
                return null;
            }
        }
    });

    @Test
    public void testAllAddressCache(){

        System.out.println("addr1 getIfPresent:" +  AllAddressCache.getIfPresent("0x01"));
        System.out.println("addr1:" + AllAddressCache.get("0x"));
        System.out.println("addr1 after loaded:" +  AllAddressCache.getIfPresent("0x01"));

        System.out.println("addr2 getIfPresent:" +  AllAddressCache.getIfPresent("test"));
        System.out.println("addr2:" + AllAddressCache.get("test"));
        System.out.println("addr2 after loaded:" +  AllAddressCache.getIfPresent("test"));

    }

}
