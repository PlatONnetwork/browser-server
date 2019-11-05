package com.platon.browser.common.complement.dto.proposal;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@Slf4j
@Accessors(chain = true)
public class ProposalVote extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_VOTE;
    }
}
