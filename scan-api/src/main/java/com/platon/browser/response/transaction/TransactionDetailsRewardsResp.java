package com.platon.browser.response.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 交易详情领取奖励子结构体返回对象
 *  @file TransactionDetailsRewardsResp.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionDetailsRewardsResp {

	private String verify;        //节点id
	private String nodeName;       //节点名称
	private BigDecimal reward;       //奖励
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getReward() {
		return reward;
	}
	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}

}
