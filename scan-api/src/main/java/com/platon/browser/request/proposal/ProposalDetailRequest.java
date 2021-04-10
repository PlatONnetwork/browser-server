package com.platon.browser.request.proposal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 提案详情请求对象
 *  @file ProposalDetailRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class ProposalDetailRequest {
    @NotBlank(message = "{proposalHash not null}")
    @Size(min = 60,max = 66)
    private String proposalHash;

	public String getProposalHash() {
		return proposalHash;
	}

	public void setProposalHash(String proposalHash) {
		this.proposalHash = proposalHash;
	}
    
}