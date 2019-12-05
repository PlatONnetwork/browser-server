package com.platon.browser.collection.queue.event;

import com.platon.browser.client.ReceiptResult;
import com.platon.browser.common.collection.dto.EpochMessage;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

/**
 * 区块搜集事件
 */
public class BlockEvent {
    // 当前原生区块的Future
    private CompletableFuture<PlatonBlock> blockCF;
    // 当前原生区块内所有交易回执的Future
    private CompletableFuture<ReceiptResult> receiptCF;
    // 当前区块相关的所有事件信息(共识周期切换事件/结算周期切换事件/增发周期切换事件)
    private EpochMessage epochMessage;

    public CompletableFuture<PlatonBlock> getBlockCF() {
        return blockCF;
    }

    public void setBlockCF(CompletableFuture<PlatonBlock> blockCF) {
        this.blockCF = blockCF;
    }

    public CompletableFuture<ReceiptResult> getReceiptCF() {
        return receiptCF;
    }

    public void setReceiptCF(CompletableFuture<ReceiptResult> receiptCF) {
        this.receiptCF = receiptCF;
    }

    public EpochMessage getEpochMessage() {
        return epochMessage;
    }

    public void setEpochMessage(EpochMessage epochMessage) {
        this.epochMessage = epochMessage;
    }
}
