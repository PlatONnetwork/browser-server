package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.service.redis.RedisBlockService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 区块入库redis处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<BlockEvent> {

    @Setter
    @Getter
    @Value("${disruptor.queue.block.batch-size}")
    private volatile int batchSize;
    @Autowired
    private RedisBlockService redisBlockService;

    private Set<Block> blockStage = new HashSet<>();

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BlockEventHandler")
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        long startTime = System.currentTimeMillis();

        log.debug("BlockEvent处理:{}(event(blockCF({}),transactions({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlockCF().toString(),event.getBlockCF().get().getNum(),sequence,endOfBatch);
        try{

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(blockStage.size()<batchSize) {
                return;
            }

            redisBlockService.save(blockStage,false);
            blockStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}