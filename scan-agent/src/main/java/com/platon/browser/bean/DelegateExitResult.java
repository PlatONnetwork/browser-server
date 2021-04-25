package com.platon.browser.bean;

import com.platon.browser.dao.param.ppos.DelegateExit;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DelegateExitResult {
    private DelegateExit delegateExit;
    private DelegationReward delegationReward;
}
