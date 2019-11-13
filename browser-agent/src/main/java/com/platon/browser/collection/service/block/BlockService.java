package com.platon.browser.collection.service.block;

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
                return retryService.getBlock(blockNumber);
            } catch (Exception e) {
                log.error("重试采集区块({})异常:",blockNumber,e);
            }
            return null;
        });
    }

    public void checkBlockNumber(Long blockNumber){
        try {
            retryService.checkBlockNumber(blockNumber);
        } catch (Exception e) {
            log.error("重试同步链出错:",e);
        }
    }
}
