package com.platon.browser.common.complement.dto.proposal;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class ProposalVersion extends BusinessParam {
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_VERSION;
    }
}
