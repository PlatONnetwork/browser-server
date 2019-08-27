package com.platon.browser.res.block;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class BlockListResp {
    private Long number;
    private Long timestamp;
    private Long serverTime;
    private Integer statTxQty;
    private Integer size;
    private String nodeName;
    private String nodeId;
    private String gasUsed;
    private String statTxGasLimit;
    private String blockReward;
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
	public Long getServerTime() {
		return serverTime;
	}
	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	public Integer getStatTxQty() {
		return statTxQty;
	}
	public void setStatTxQty(Integer statTxQty) {
		this.statTxQty = statTxQty;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
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
    
}
