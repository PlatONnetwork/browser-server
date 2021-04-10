package com.platon.browser.response.proposal;

/**
 *  投票列表返回对象
 *  @file VoteListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class VoteListResp {
	private String voterName;          //投票验证人名称
	private String voter;            //投票验证人
	private String option;            //投票选型  1：支持；  2：反对；  3弃权
	private String txHash;       //投票的hash
	private Long timestamp;         //投票的时间
	public String getVoterName() {
		return voterName;
	}
	public void setVoterName(String voterName) {
		this.voterName = voterName;
	}
	public String getVoter() {
		return voter;
	}
	public void setVoter(String voter) {
		this.voter = voter;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
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
	
}
