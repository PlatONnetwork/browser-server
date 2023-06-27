package com.platon.browser.request.newblock;

import com.platon.browser.request.PageReq;
import com.platon.browser.utils.HexUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *  查询节点id请求对象
 *  @file BlockListByNodeIdReq.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockListByNodeIdReq extends PageReq{
	@NotBlank(message="{nodeId is not null}")
	@Size(min = 130,max = 130)
    private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = HexUtil.prefix(nodeId.toLowerCase());
	}

}
