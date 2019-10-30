package com.platon.browser.complement.queue.callback;

import com.platon.browser.queue.callback.ConsumeCallback;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class CollectionBlockCallback implements ConsumeCallback<CollectionBlockEvent> {
    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void call(CollectionBlockEvent cbe) throws ExecutionException, InterruptedException {
        log.info("block number:{}",cbe.getBody().getBlockCF().get().getBlock().getNumber());
    }
}
