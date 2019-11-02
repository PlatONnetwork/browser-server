package com.platon.browser.queue.collection.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.queue.collection.event.CollectionEvent;
import org.springframework.retry.annotation.Retryable;


/**
 * 区块事件处理器接口
 */
public interface ICollectionEventHandler extends EventHandler<CollectionEvent> {

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    void onEvent(CollectionEvent event, long sequence, boolean endOfBatch);
}