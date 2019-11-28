package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.queue.event.TransactionEvent;
import com.platon.browser.service.redis.RedisTransactionService;
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
 * 交易入库redis处理器
 */
@Slf4j
@Component
public class TransactionEventHandler implements EventHandler<TransactionEvent> {

    @Setter
    @Getter
    @Value("${disruptor.queue.transaction.batch-size}")
    private volatile int batchSize;
    @Autowired
    private RedisTransactionService redisTransactionService;

    private Set<Transaction> transactionStage = new HashSet<>();
    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "TransactionEventHandler")
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        long startTime = System.currentTimeMillis();

        log.debug("TransactionEvent处理:{}(event(blockCF({}),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getTransactionCF().get().getNum(),sequence,endOfBatch);
        try{

            // 如区块暂存区的区块数量达不到批量入库大小,则返回
            if(transactionStage.size()<batchSize) {
                return;
            }

            redisTransactionService.save(transactionStage,false);
            transactionStage.clear();

        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}