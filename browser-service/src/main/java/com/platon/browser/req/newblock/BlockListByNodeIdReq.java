package com.platon.browser.req.newblock;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 *  查询节点id请求对象
 *  @file BlockListByNodeIdReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockListByNodeIdReq extends PageReq{
	@NotBlank(message="{nodeId is not null}")
    private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}