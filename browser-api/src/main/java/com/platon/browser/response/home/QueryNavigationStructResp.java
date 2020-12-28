package com.platon.browser.response.home;

/**
 * 首页返回查询对象子结构体对象
 *  @file QueryNavigationStructResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class QueryNavigationStructResp {
	private Long number; // 区块高度
	private String txHash; // 交易hash
	private String address; // 地址
	private String nodeId; // 节点地址
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
