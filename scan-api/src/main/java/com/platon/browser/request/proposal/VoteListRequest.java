package com.platon.browser.request.proposal;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 投票请求对象
 *  @file VoteListRequest.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class VoteListRequest extends PageReq{
	@NotBlank(message = "{proposalHash not null}")
	@Size(min = 60,max = 66)
    private String proposalHash;

    private String option;

	public String getProposalHash() {
		return proposalHash;
	}

	public void setProposalHash(String proposalHash) {
		this.proposalHash = proposalHash;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

}
