package com.platon.browser.common.complement.param.proposal;

import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class ProposalVersion extends BusinessParam {

	private String optDesc;
	
    @Override
    public BusinessType getBusinessType() {
        return BusinessType.PROPOSAL_VERSION;
    }
}
