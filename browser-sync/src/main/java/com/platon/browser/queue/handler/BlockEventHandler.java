package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.service.redis.RedisBlockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * 区块入库redis处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<List<Block>> {

    @Autowired
    private RedisBlockService redisBlockService;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BlockEventHandler")
    public void onEvent(List<Block> event, long sequence, boolean endOfBatch){
        long startTime = System.currentTimeMillis();

        log.debug("BlockEvent处理:{}(blocks({}),sequence({}),endOfBatch({})",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.size(),sequence,endOfBatch);
        try{
            redisBlockService.save(new HashSet<>(event),false);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}