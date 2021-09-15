package com.platon.browser.bean;

import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * 区块搜集事件
 */
@Data
public class BlockEvent {

    /**
     * 链路ID
     */
    private String traceId;

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


    /**
     * 释放对象引用
     */
    public void releaseRef() {
        blockCF = null;
        receiptCF = null;
        epochMessage = null;
    }
}
