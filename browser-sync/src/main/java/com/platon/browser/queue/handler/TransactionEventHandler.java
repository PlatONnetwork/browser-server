package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.service.redis.RedisTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 交易入库redis处理器
 */
@Slf4j
@Component
public class TransactionEventHandler implements EventHandler<List<Transaction>> {

    @Autowired
    private RedisTransactionService redisTransactionService;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "TransactionEventHandler")
    public void onEvent(List<Transaction> event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        long startTime = System.currentTimeMillis();

        log.debug("TransactionEvent处理:{}(event(transactions({}),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.size(),sequence,endOfBatch);
        try{

            redisTransactionService.save(new HashSet<>(event),false);

        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}