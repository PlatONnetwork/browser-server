package com.platon.browser.complement.queue.handler;

import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CollectionBlockEventRetryHandler {
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    boolean isDone(CollectionBlockEvent event) {
        log.debug("{}({})",Thread.currentThread().getStackTrace()[1].getMethodName(),event.hashCode());
        return event.getBody().getBlockCF().isDone()&&event.getBody().getReceiptCF().isDone();
    }
}