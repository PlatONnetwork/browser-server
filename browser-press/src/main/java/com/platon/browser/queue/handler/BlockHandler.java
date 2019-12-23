package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.queue.event.BlockEvent;
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockHandler implements EventHandler<BlockEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Setter
    @Getter
    @Value("${disruptor.queue.block.batch-size}")
    private volatile int batchSize;

    private Set<Block> blockStage = new HashSet<>();

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        log.debug("BlockEvent处理:{}(event(blockList({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlockList().size());
        try {
            blockStage.addAll(event.getBlockList());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(blockStage.size()<batchSize) {
                return;
            }

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(blockStage, Collections.emptySet(),Collections.emptySet());
            // 入库Redis 更新Redis中的统计记录
            Set<NetworkStat> statistics = new HashSet<>();
            redisImportService.batchImport(blockStage,Collections.emptySet(),statistics);
            blockStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}