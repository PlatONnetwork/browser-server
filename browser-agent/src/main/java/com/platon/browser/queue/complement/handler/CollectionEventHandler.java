package com.platon.browser.queue.complement.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.queue.collection.event.CollectionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class CollectionEventHandler implements EventHandler<CollectionEvent> {

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException {

        log.info("Block Number: {}", event.getBlock().getNum());
        log.info("Transactions: {}", event.getTransactions());
        log.info("Epoch Messages: {}", event.getEpochMessage());

    }
}