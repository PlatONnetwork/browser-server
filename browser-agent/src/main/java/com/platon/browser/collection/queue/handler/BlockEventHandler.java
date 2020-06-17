package com.platon.browser.collection.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.collection.queue.event.BlockEvent;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.queue.collection.publisher.CollectionEventPublisher;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<BlockEvent> {

    @Autowired
    private CollectionEventPublisher collectionEventPublisher;
    @Autowired
    private PlatOnClient platOnClient;
    @Autowired
    private AddressCache addressCache;
    @Autowired
    private SpecialApi specialApi;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BlockEventHandler")
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException, ContractInvokeException, BlankResponseException {
        long startTime = System.currentTimeMillis();

        log.debug("BlockEvent处理:{}(event(block({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getEpochMessage().getCurrentBlockNumber(),sequence,endOfBatch);
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            ReceiptResult receiptResult = event.getReceiptCF().get();
            CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(rawBlock,receiptResult,platOnClient,addressCache,specialApi);
            block.setReward(event.getEpochMessage().getBlockReward().toString());

            collectionEventPublisher.publish(block,block.getTransactions(),event.getEpochMessage());

            // 释放对象引用
            event.releaseRef();
        }catch (Exception e){
            log.error("onEvent error",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}