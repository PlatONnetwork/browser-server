package com.platon.browser.response.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;
import java.util.List;

/**
 * 奖励领取返回对象
 *  @file QueryClaimByAddressResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年12月31日
 */
public class QueryClaimByAddressResp {
	private String txHash;    //交易hash
	private Long timestamp;//提取时间时间
    private BigDecimal allRewards;      //总收益
    private List<TransactionDetailsRewardsResp> rewardsDetails;        //交易子结构体
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getAllRewards() {
		return allRewards;
	}
	public void setAllRewards(BigDecimal allRewards) {
		this.allRewards = allRewards;
	}
	public List<TransactionDetailsRewardsResp> getRewardsDetails() {
		return rewardsDetails;
	}
	public void setRewardsDetails(List<TransactionDetailsRewardsResp> rewardsDetails) {
		this.rewardsDetails = rewardsDetails;
	}

	
}
