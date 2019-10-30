package com.platon.browser.complement.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * 搜集区块事件处理器
 */
@Slf4j
@Component
public class CollectionBlockEventHandler implements EventHandler<CollectionBlockEvent> {

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(CollectionBlockEvent event, long sequence, boolean endOfBatch) {
        // 等待CompletableFuture完成
        log.debug("{}:sequence({}),endOfBatch({})",Thread.currentThread().getStackTrace()[1].getMethodName(),sequence,endOfBatch);
        if(!event.getBody().getBlockCF().isDone()){
            throw new BusinessException("区块采集线程未完成,稍后重新检查!");
        }
        if(!event.getBody().getReceiptCF().isDone()){
            throw new BusinessException("交易回执线程未完成,稍后重新检查!");
        }
        try {
            // 调用回调处理业务
            event.getBody().getEventCB().call(event);
        } catch (Exception e) {
            log.error("调用回调处理业务出错:",e);
        }
    }
}