package com.platon.browser.response.proposal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomVersionSerializer;


/**
 *提案列表返回对象
 *  @file ProposalListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class ProposalListResp {
	private String pipNum; //pip num
	private String proposalHash; // 提案内部标识
	private String topic;  //提案标题
	private String description;  //提案描述
	private String url; // github地址 https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md PIP编号
						// eip-100
	private String type; // 提案类型 1：文本提案； 2：升级提案； 3参数提案。
	private String status; // 状态 1：投票中 2：通过 3：失败 4：预升级 5：升级完成 已通过=2 或4 或 5
	private String curBlock; // 当前块高
	private String endVotingBlock; // 投票结算的快高
	private String newVersion; // 升级提案升级的版本
	private String paramName; // 参数名
	private Long timestamp; // 提案时间
	private Long inBlock; // 提案所在区块
	public String getPipNum() {
		return pipNum;
	}
	public void setPipNum(String pipNum) {
		this.pipNum = pipNum;
	}
	public String getProposalHash() {
		return proposalHash;
	}
	public void setProposalHash(String proposalHash) {
		this.proposalHash = proposalHash;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurBlock() {
		return curBlock;
	}
	public void setCurBlock(String curBlock) {
		this.curBlock = curBlock;
	}
	public String getEndVotingBlock() {
		return endVotingBlock;
	}
	public void setEndVotingBlock(String endVotingBlock) {
		this.endVotingBlock = endVotingBlock;
	}
	@JsonSerialize(using = CustomVersionSerializer.class)
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getInBlock() {
		return inBlock;
	}
	public void setInBlock(Long inBlock) {
		this.inBlock = inBlock;
	}
	
}
