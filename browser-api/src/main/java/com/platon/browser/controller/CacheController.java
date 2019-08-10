/*
package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.service.cache.BlockCacheService;
import com.platon.browser.service.cache.NodeCacheService;
import com.platon.browser.service.cache.StatisticCacheService;
import com.platon.browser.service.cache.TransactionCacheService;
import com.platon.browser.util.CacheEnum;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.CAST5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 *//*

@RestController
@RequestMapping("/cache")
public class CacheController {
    private static Logger logger = LoggerFactory.getLogger(CacheController.class);
    @Autowired
    private BlockCacheService blockCacheService;
    @Autowired
    private NodeCacheService nodeCacheService;
    @Autowired
    private TransactionCacheService transactionCacheService;
    @Autowired
    private StatisticCacheService statisticCacheService;
    @Autowired
    private ChainsConfig chainsConfig;

    @GetMapping("reset/{chainId}/{cacheName}/{clearOld}")
    public String reset(@PathVariable String chainId, @PathVariable String cacheName, @PathVariable boolean clearOld){
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
                case NODE:nodeCacheService.resetNodePushCache(chainId,clearOld);break;
                case BLOCK:blockCacheService.resetBlockCache(chainId,clearOld);break;
                case TRANSACTION:transactionCacheService.resetTransactionCache(chainId,clearOld);break;
                case STATISTICS:statisticCacheService.clearStatisticsCache(chainId);break;
                case LIST:transactionCacheService.clearTranListCache(chainId);break;
            }
        }catch (Exception ex){
            return "Reset cache ["+cacheName+"] of chain ["+chainId+"] failed";
        }
        return "Cache ["+cacheName+"] of Chain ["+chainId+"] reset successfully!";
    }

    @GetMapping("setup/{chainId}/{cacheName}/{value}")
    public String setup(@PathVariable String chainId, @PathVariable String cacheName, @PathVariable String value){
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
                case MAXTPS:statisticCacheService.updateMaxTps(chainId,value);break;
            }
        }catch (Exception ex){
            return "Reset cache ["+cacheName+"] of chain ["+chainId+"] failed";
        }
        return "Cache ["+cacheName+"] of Chain ["+chainId+"] reset successfully!";
    }
}
*/
