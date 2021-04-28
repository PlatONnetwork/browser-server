package com.platon.browser.bootstrap;

import com.platon.browser.bean.ReceiptResult;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * 自检事件
 */
@Data
public class BootstrapEvent {

    private String traceId;

    // 当前原生区块的Future
    private CompletableFuture<PlatonBlock> blockCF;

    // 当前原生区块内所有交易回执的Future
    private CompletableFuture<ReceiptResult> receiptCF;

    // 处理完区块后的回调
    private Callback callback;

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

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * 释放对象引用
     */
    public void releaseRef() {
        blockCF = null;
        receiptCF = null;
        callback = null;
    }

}
