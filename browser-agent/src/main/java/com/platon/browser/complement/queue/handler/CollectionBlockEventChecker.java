package com.platon.browser.complement.queue.handler;

import com.platon.browser.exception.BusinessException;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;


/**
 * 事件中的CompletableFuture检查器
 */
@Slf4j
@Component
public class CollectionBlockEventChecker {
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    void isDone(CollectionBlockEvent event) {
        log.debug("{}({})",Thread.currentThread().getStackTrace()[1].getMethodName(),event.hashCode());
        if(!event.getBody().getBlockCF().isDone()){
            throw new BusinessException("区块采集线程未完成,稍后重新检查!");
        }
        if(!event.getBody().getReceiptCF().isDone()){
            throw new BusinessException("交易回执线程未完成,稍后重新检查!");
        }
    }
}