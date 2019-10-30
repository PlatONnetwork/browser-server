package com.platon.browser.collection.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/30 10:14
 * @Description: 区块服务
 */
@Slf4j
@Service
public class BlockService {

    @Autowired
    private BlockRetryService retryService;

    /**
     * 异步获取区块
     */
    public CompletableFuture<PlatonBlock> getBlockAsync(Long blockNumber) {
        return CompletableFuture.supplyAsync(()->{
            try {
                PlatonBlock block = retryService.getBlock(blockNumber);
                return block;
            } catch (Exception e) {
                log.error("采集区块({})异常!",blockNumber,e);
            }
            return null;
        });
    }
}
