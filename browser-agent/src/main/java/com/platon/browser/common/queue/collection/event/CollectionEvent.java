package com.platon.browser.common.queue.collection.event;

import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class CollectionEvent {
    // 区块信息
    private Block block;
    // 交易列表
    private List<Transaction> transactions;
    private EpochMessage epochMessage;
}
