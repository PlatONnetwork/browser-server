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
    private CompletableFuture<PlatonBlock> blockCF;
    private CompletableFuture<ReceiptResult> receiptCF;
    private ConsumeCallback<PlatonBlock> blockCB;
    private ConsumeCallback<ReceiptResult> receiptCB;
}
