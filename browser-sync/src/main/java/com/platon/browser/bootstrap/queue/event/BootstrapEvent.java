package com.platon.browser.bootstrap.queue.event;

import com.platon.browser.bootstrap.queue.callback.Callback;
import com.platon.browser.client.ReceiptResult;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.concurrent.CompletableFuture;

/**
 * 自检事件
 */
@Data
@Builder
@Accessors(chain = true)
public class BootstrapEvent {
    // 当前原生区块的Future
    private CompletableFuture<PlatonBlock> blockCF;
    // 当前原生区块内所有交易回执的Future
    private CompletableFuture<ReceiptResult> receiptCF;
    // 处理完区块后的回调
    private Callback callback;
}
