package com.platon.browser.res.block;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

import lombok.Data;

public class BlockDetailResp {
    private Long number;
    private Long timestamp;
    private Integer statTxQty;
    private String hash;
    private String parentHash;
    private String nodeName;
    private String nodeId;
    private Long timeDiff;
    private String gasLimit;
    private String gasUsed;
    private String statTxGasLimit;
    private String blockReward;
    private String extraData;
 // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
    private Integer statTransferQty;
    private Integer statDelegateQty;
    private Integer statStakingQty;
    private Integer statProposalQty;
    private Long serverTime;
    private Integer size;
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getStatTxQty() {
		return statTxQty;
	}
	public void setStatTxQty(Integer statTxQty) {
		this.statTxQty = statTxQty;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getParentHash() {
		return parentHash;
	}
	public void setParentHash(String parentHash) {
		this.parentHash = parentHash;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public Long getTimeDiff() {
		return timeDiff;
	}
	public void setTimeDiff(Long timeDiff) {
		this.timeDiff = timeDiff;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
	public String getStatTxGasLimit() {
		return statTxGasLimit;
	}
	public void setStatTxGasLimit(String statTxGasLimit) {
		this.statTxGasLimit = statTxGasLimit;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getBlockReward() {
		return blockReward;
	}
	public void setBlockReward(String blockReward) {
		this.blockReward = blockReward;
	}
	public String getExtraData() {
		return extraData;
	}
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
	public Integer getStatTransferQty() {
		return statTransferQty;
	}
	public void setStatTransferQty(Integer statTransferQty) {
		this.statTransferQty = statTransferQty;
	}
	public Integer getStatDelegateQty() {
		return statDelegateQty;
	}
	public void setStatDelegateQty(Integer statDelegateQty) {
		this.statDelegateQty = statDelegateQty;
	}
	public Integer getStatStakingQty() {
		return statStakingQty;
	}
	public void setStatStakingQty(Integer statStakingQty) {
		this.statStakingQty = statStakingQty;
	}
	public Integer getStatProposalQty() {
		return statProposalQty;
	}
	public void setStatProposalQty(Integer statProposalQty) {
		this.statProposalQty = statProposalQty;
	}
	public Long getServerTime() {
		return serverTime;
	}
	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
    
}
