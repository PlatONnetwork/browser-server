package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 自检事件
 */
@Data
@Builder
@Accessors(chain = true)
public class TransactionEvent {
    private List <Transaction> transactions;
}
