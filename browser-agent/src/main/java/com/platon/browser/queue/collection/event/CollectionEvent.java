package com.platon.browser.queue.collection.event;

import com.platon.browser.common.dto.CollectionBlock;
import com.platon.browser.common.dto.CollectionTransaction;
import com.platon.browser.common.dto.EpochMessage;
import lombok.Data;

import java.util.List;

@Data
public class CollectionEvent {
    // 区块信息
    private CollectionBlock block;
    // 交易列表
    private List<CollectionTransaction> transactions;
    private EpochMessage epochMessage;
}
