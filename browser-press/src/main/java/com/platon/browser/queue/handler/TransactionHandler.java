package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.queue.event.TransactionEvent;
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
public class TransactionHandler implements EventHandler<TransactionEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;

    private Set<Transaction> transactionStage = new HashSet<>();

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        log.debug("TransactionEvent处理:{}(event(transactionList({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getTransactionList().size());
        try {
            transactionStage.addAll(event.getTransactionList());

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(transactionStage.size()<batchSize) {
                return;
            }

            // 入库ES 入库节点操作记录到ES
            esImportService.batchImport(Collections.emptySet(),transactionStage,Collections.emptySet());
            // 入库Redis 更新Redis中的统计记录
//            Set<NetworkStat> statistics = new HashSet<>();
//            redisImportService.batchImport(Collections.emptySet(),transactionStage,statistics);
            transactionStage.clear();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}