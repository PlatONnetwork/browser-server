package com.platon.browser.res.staking;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 * 根据节点查询领取奖励
 *  @file QueryClaimByStakingResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月31日
 */
public class QueryClaimByStakingResp {

	public String hash;
	
	public String addr;
	
	public Long time;
	
	public BigDecimal reward;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
	
}
