package com.platon.browser.req.proposal;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 * 投票请求对象
 *  @file VoteListRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class VoteListRequest extends PageReq{
	@NotBlank
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