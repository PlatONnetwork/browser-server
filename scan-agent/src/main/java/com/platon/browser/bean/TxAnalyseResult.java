package com.platon.browser.bean;

import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class TxAnalyseResult {
    private List<NodeOpt> nodeOptList;
    private List<DelegationReward> delegationRewardList;
    private int proposalQty;
}
