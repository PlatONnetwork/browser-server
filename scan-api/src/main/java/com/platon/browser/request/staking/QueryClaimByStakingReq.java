package com.platon.browser.request.staking;

import com.platon.browser.request.PageReq;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 根据质押的请求对象
 *  @file QueryClaimByStakingReq.java
 *  @description
 *	@author zhangrj
 *  @data 2019年12月31日
 */
public class QueryClaimByStakingReq extends PageReq {

	@NotBlank(message = "{nodeId not null}")
	@Size(min = 130,max = 130)
	private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
