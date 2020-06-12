package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

/**
 * 验证人委托列表请求对象
 *  @file DelegationListByStakingReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class DelegationListByStakingReq extends PageReq{
	@NotBlank(message = "{nodeId not null}")
    private String nodeId;
    private String stakingBlockNum;
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getStakingBlockNum() {
		return stakingBlockNum;
	}
	public void setStakingBlockNum(String stakingBlockNum) {
		this.stakingBlockNum = stakingBlockNum;
	}
}