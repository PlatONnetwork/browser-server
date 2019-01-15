package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.CacheEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
    private static Logger logger = LoggerFactory.getLogger(CacheController.class);
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ChainsConfig chainsConfig;

    @GetMapping("reset/{chainId}/{cacheName}")
    public String reset(@PathVariable String chainId, @PathVariable String cacheName){
        if(StringUtils.isBlank(chainId)){
            return "Please provide the chain id.";
        }
        if(!chainsConfig.getChainIds().contains(chainId.trim())){
            return "Chain id ["+chainId+"] incorrect.";
        }
        if(StringUtils.isBlank(cacheName)){
            return "Please provide the cache name.";
        }
        try {
            CacheEnum cacheEnum = CacheEnum.valueOf(cacheName.toUpperCase());
            switch (cacheEnum){
                case NODE:redisCacheService.resetNodePushCache(chainId);break;
                case BLOCK:redisCacheService.resetBlockCache(chainId);break;
                case TRANSACTION:redisCacheService.resetTransactionCache(chainId);break;
            }
        }catch (Exception ex){
            return "Reset cache ["+cacheName+"] of chain ["+chainId+"] failed";
        }
        return "Cache ["+cacheName+"] of Chain ["+chainId+"] reset successfully!";
    }
}