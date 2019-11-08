package com.platon.browser.common.queue.complement.event;

import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class ComplementEvent {
    // 区块信息
    private Block block;
    // 交易列表
    private List<Transaction> transactions;
    // 交易列表
    private List<NodeOpt> nodeOpts;
}
