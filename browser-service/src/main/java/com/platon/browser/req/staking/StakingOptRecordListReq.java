package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 * 验证人操作列表请求对象
 *  @file StakingOptRecordListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingOptRecordListReq extends PageReq{
	@NotBlank(message = "{nodeId not null}")
    private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}