package com.platon.browser.collection.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.collection.queue.event.BlockEvent;
import com.platon.browser.common.dto.CollectionBlock;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.queue.collection.publisher.CollectionEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.ExecutionException;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<BlockEvent> {

    @Autowired
    private CollectionEventPublisher collectionEventPublisher;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
        ReceiptResult receiptResult = event.getReceiptCF().get();
        CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(rawBlock,receiptResult);
        collectionEventPublisher.publish(block,block.getTransactions(),event.getEpochMessage());
    }
}