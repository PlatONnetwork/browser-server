package com.platon.browser.persistence.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.persistence.queue.event.PersistenceEvent;
import com.platon.browser.persistence.service.elasticsearch.EsImportService;
import com.platon.browser.persistence.service.RedisService;
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
public class PersistenceEventHandler implements EventHandler<PersistenceEvent> {

    @Autowired
    private EsImportService esService;
    @Autowired
    private RedisService redisService;

    @Value("${disruptor.queue.persistence.batch-size}")
    private int batchSize;

    private Set<Block> blockStage = new HashSet<>();
    private Set<Transaction> transactionStage = new HashSet<>();

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "PersistenceEventHandler")
    public void onEvent(PersistenceEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        try {
            blockStage.add(event.getBlock());
            transactionStage.addAll(event.getTransactions());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(blockStage.size()<batchSize) return;

            // 入库ES
            esService.batchImport(blockStage,transactionStage, Collections.emptySet(),Collections.emptySet());
            // 入库Redis
            redisService.batchInsertOrUpdate(blockStage,transactionStage);
            blockStage.clear();
            transactionStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }
}