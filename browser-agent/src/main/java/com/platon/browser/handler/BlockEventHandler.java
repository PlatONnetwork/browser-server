package com.platon.browser.handler;

import com.platon.protocol.core.methods.response.PlatonBlock;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.bean.BlockEvent;
import com.platon.browser.publisher.CollectionEventPublisher;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import com.platon.browser.service.erc20.Erc20ResolveServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BlockEventHandler implements EventHandler<BlockEvent> {

    @Resource
    private CollectionEventPublisher collectionEventPublisher;
    @Resource
    private PlatOnClient platOnClient;
    @Resource
    private AddressCache addressCache;
    @Resource
    private SpecialApi specialApi;
    @Resource
    private Erc20ResolveServiceImpl erc20ResolveServiceImpl;

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, label = "BlockEventHandler")
    public void onEvent(BlockEvent event, long sequence, boolean endOfBatch)
        throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException,
        ContractInvokeException, BlankResponseException {
        long startTime = System.currentTimeMillis();

        log.debug("BlockEvent处理:{}(event(block({})),sequence({}),endOfBatch({}))",
            Thread.currentThread().getStackTrace()[1].getMethodName(), event.getEpochMessage().getCurrentBlockNumber(),
            sequence, endOfBatch);
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            ReceiptResult receiptResult = event.getReceiptCF().get();
            CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(rawBlock,
                receiptResult, this.platOnClient, this.addressCache, this.specialApi, this.erc20ResolveServiceImpl);
            this.erc20ResolveServiceImpl.initContractAddressCache(block.getTransactions(), this.addressCache);
            block.setReward(event.getEpochMessage().getBlockReward().toString());

            this.collectionEventPublisher.publish(block, block.getTransactions(), event.getEpochMessage());

            // 释放对象引用
            event.releaseRef();
        } catch (Exception e) {
            log.error("onEvent error", e);
            throw e;
        }

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
    }
}