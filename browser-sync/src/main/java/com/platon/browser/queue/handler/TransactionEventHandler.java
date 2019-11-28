package com.platon.browser.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.queue.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 自检事件处理器
 */
@Slf4j
@Component
public class TransactionEventHandler implements EventHandler<TransactionEvent> {


    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BootstrapEventHandler")
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        long startTime = System.currentTimeMillis();

        log.debug("BootstrapEvent处理:{}(event(blockCF({}),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getTransactionCF().get().getNum(),sequence,endOfBatch);
        try{

        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}