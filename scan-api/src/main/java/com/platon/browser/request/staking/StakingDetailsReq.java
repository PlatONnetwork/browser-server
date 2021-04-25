package com.platon.browser.request.staking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

import com.platon.browser.utils.HexUtil;

/**
 * 验证人详情请求对象
 *  @file StakingDetailsReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingDetailsReq {
    @NotBlank(message = "{nodeId not null}")
    @Size(min = 128,max = 130)
    private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		if(StringUtils.isBlank(nodeId)) return;
		this.nodeId = HexUtil.prefix(nodeId.toLowerCase());
	}
    
}