package com.platon.browser.persistence.queue.event;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 区块&交易持久化事件
 */
@Data
@Builder
@Accessors(chain = true)
public class PersistenceEvent {
    // 区块
    private Block block;
    // 交易
    private List<Transaction> transactions;
}
