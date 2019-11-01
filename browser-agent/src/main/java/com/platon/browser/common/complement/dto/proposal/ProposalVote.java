package com.platon.browser.common.complement.dto.proposal;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class ProposalVote extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_VOTE;
    }
}
