package com.platon.browser.common.queue.complement.event;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class ComplementEvent {
    // 区块信息
    private CollectionBlock block;
    // 交易列表
    private List<CollectionTransaction> transactions;
    // 交易列表
    private List<ComplementNodeOpt> nodeOpts;
    // 业务参数
    private List<BusinessParam> businessParams;
}
