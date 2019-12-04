package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

/**
 * 验证人详情请求对象
 *  @file StakingDetailsReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingDetailsReq {
    @NotBlank(message = "{nodeId not null}")
    private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
    
}