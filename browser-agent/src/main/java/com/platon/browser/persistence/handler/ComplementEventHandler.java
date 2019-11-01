package com.platon.browser.persistence.handler;

import com.platon.browser.queue.complement.event.ComplementEvent;
import com.platon.browser.queue.complement.handler.IComplementEventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 区块事件处理器
 */
@Slf4j
public class ComplementEventHandler implements IComplementEventHandler {
    @Override
    public void onEvent(ComplementEvent event, long sequence, boolean endOfBatch) {
        // 此处调用 persistence模块功能

        log.info("Block Number: {}", event.getBlock().getNum());
        log.info("Transactions: {}", event.getTransactions());
        log.info("Business Params: {}", event.getBusinessParams());
    }
}