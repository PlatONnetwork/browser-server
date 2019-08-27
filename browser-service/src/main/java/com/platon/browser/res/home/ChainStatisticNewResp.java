package com.platon.browser.res.home;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class ChainStatisticNewResp {
	private Long currentNumber; // 当前区块高度
	private String nodeId; // 出块节点id
	private String nodeName; // 出块节点名称
	private Integer txQty; // 总的交易数
	private Integer currentTps; // 当前的TPS
	private Integer maxTps; // 最大交易TPS
	private String turnValue; // 当前流通量
	private String issueValue; // 当前发行量
	private String stakingDelegationValue; // 当前质押总数=有效的质押+委托
	private Integer addressQty; // 地址数
	private Integer proposalQty; // 总提案数
	private Integer doingProposalQty; // 进行中提案数
	public Long getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(Long currentNumber) {
		this.currentNumber = currentNumber;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Integer getTxQty() {
		return txQty;
	}
	public void setTxQty(Integer txQty) {
		this.txQty = txQty;
	}
	public Integer getCurrentTps() {
		return currentTps;
	}
	public void setCurrentTps(Integer currentTps) {
		this.currentTps = currentTps;
	}
	public Integer getMaxTps() {
		return maxTps;
	}
	public void setMaxTps(Integer maxTps) {
		this.maxTps = maxTps;
	}
	public String getTurnValue() {
		return turnValue;
	}
	public void setTurnValue(String turnValue) {
		this.turnValue = turnValue;
	}
	public String getIssueValue() {
		return issueValue;
	}
	public void setIssueValue(String issueValue) {
		this.issueValue = issueValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getStakingDelegationValue() {
		return stakingDelegationValue;
	}
	public void setStakingDelegationValue(String stakingDelegationValue) {
		this.stakingDelegationValue = stakingDelegationValue;
	}
	public Integer getAddressQty() {
		return addressQty;
	}
	public void setAddressQty(Integer addressQty) {
		this.addressQty = addressQty;
	}
	public Integer getProposalQty() {
		return proposalQty;
	}
	public void setProposalQty(Integer proposalQty) {
		this.proposalQty = proposalQty;
	}
	public Integer getDoingProposalQty() {
		return doingProposalQty;
	}
	public void setDoingProposalQty(Integer doingProposalQty) {
		this.doingProposalQty = doingProposalQty;
	}
	
	
}
