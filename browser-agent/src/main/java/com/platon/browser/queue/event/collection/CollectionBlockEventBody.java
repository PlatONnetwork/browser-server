package com.platon.browser.queue.event.collection;

import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.queue.callback.ConsumeCallback;
import lombok.Data;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

/**
 * 区块搜集事件主体
 */
@Data
public class CollectionBlockEventBody {
    // 当前原生区块的Future
    private CompletableFuture<PlatonBlock> blockCF;
    // 当前原生区块内所有交易回执的Future
    private CompletableFuture<ReceiptResult> receiptCF;
    // 当前原生区块的处理回调, 由环形队生产端指定，由消费端调用
    private ConsumeCallback<CollectionBlockEvent> eventCB;
    // 当前区块相关的所有事件信息(共识周期切换事件/结算周期切换事件/增发周期切换事件)
    private EpochMessage epochMessage;
}
