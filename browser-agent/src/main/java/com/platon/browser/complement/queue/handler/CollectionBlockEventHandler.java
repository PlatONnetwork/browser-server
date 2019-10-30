package com.platon.browser.complement.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.old.exception.BusinessException;
import com.platon.browser.queue.event.collection.CollectionBlockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * 搜集区块事件处理器
 */
@Slf4j
@Component
public class CollectionBlockEventHandler implements EventHandler<CollectionBlockEvent> {
    @Autowired
    private CollectionBlockEventRetryHandler retryHandler;
    @Override
    public void onEvent(CollectionBlockEvent event, long sequence, boolean endOfBatch) throws BusinessException {
        // 等待CompletableFuture完成
        retryHandler.isDone(event);
        try {
            // 调用回调处理业务
            event.getBody().getBlockCB().call(event.getBody().getBlockCF().get());
            event.getBody().getReceiptCB().call(event.getBody().getReceiptCF().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}