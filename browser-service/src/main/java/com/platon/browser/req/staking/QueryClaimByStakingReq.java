package com.platon.browser.req.staking;

import com.platon.browser.req.PageReq;

/**
 * 根据质押的请求对象
 *  @file QueryClaimByStakingReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月31日
 */
public class QueryClaimByStakingReq extends PageReq {

	private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
