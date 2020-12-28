package com.platon.browser.response.home;

/**
 * 首页区块列表返回对象
 *  @file BlockListNewResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockListNewResp {
	private Boolean isRefresh; // 是否更新
	private Long number; // 区块高度
	private Long timestamp; // 出块时间
	private Long serverTime; // 服务器时间
	private String nodeId; // 出块节点id
	private String nodeName; // 出块节点名称
	private Integer statTxQty; // 交易数
	public Boolean getIsRefresh() {
		return isRefresh;
	}
	public void setIsRefresh(Boolean isRefresh) {
		this.isRefresh = isRefresh;
	}
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
	public Integer getStatTxQty() {
		return statTxQty;
	}
	public void setStatTxQty(Integer statTxQty) {
		this.statTxQty = statTxQty;
	}

	
}
