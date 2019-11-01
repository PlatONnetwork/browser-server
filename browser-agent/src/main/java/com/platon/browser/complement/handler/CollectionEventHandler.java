package com.platon.browser.complement.handler;

import com.platon.browser.queue.collection.event.CollectionEvent;
import com.platon.browser.queue.collection.handler.ICollectionEventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 区块事件处理器
 */
@Slf4j
public class CollectionEventHandler implements ICollectionEventHandler {

    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) {

        // 此处调用 complement模块的功能

        log.info("Block Number: {}", event.getBlock().getNum());
        log.info("Transactions: {}", event.getTransactions());
        log.info("Epoch Messages: {}", event.getEpochMessage());

    }
}