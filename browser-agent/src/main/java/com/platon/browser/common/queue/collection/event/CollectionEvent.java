package com.platon.browser.common.queue.collection.event;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.collection.dto.EpochMessage;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class CollectionEvent {
    // 区块信息
    private CollectionBlock block;
    // 交易列表
    private List<CollectionTransaction> transactions;
    private EpochMessage epochMessage;
}
