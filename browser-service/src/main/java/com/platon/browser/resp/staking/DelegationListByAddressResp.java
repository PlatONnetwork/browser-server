package com.platon.browser.resp.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 * 地址锁仓列表返回对象
 *  @file DelegationListByAddressResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class DelegationListByAddressResp {
	private String nodeId;            //节点id
    private String nodeName;          //节点名称
    private String delegateValue;     //委托数量
    private String delegateHas;       //未锁定委托（LAT）
    private String delegateLocked;    //已锁定委托（LAT）
    private String allDelegateLocked; //当前验证人总接收的锁定委托量（LAT）
    private String delegateUnlock;    //已解除委托（LAT） 
    private String delegateReduction;  //赎回中委托（LAT） 
    private String delegateTotalValue;// 验证人委托的总金额
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
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateValue() {
		return delegateValue;
	}
	public void setDelegateValue(String delegateValue) {
		this.delegateValue = delegateValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateHas() {
		return delegateHas;
	}
	public void setDelegateHas(String delegateHas) {
		this.delegateHas = delegateHas;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateLocked() {
		return delegateLocked;
	}
	public void setDelegateLocked(String delegateLocked) {
		this.delegateLocked = delegateLocked;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getAllDelegateLocked() {
		return allDelegateLocked;
	}
	public void setAllDelegateLocked(String allDelegateLocked) {
		this.allDelegateLocked = allDelegateLocked;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateUnlock() {
		return delegateUnlock;
	}
	public void setDelegateUnlock(String delegateUnlock) {
		this.delegateUnlock = delegateUnlock;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateReduction() {
		return delegateReduction;
	}
	public void setDelegateReduction(String delegateReduction) {
		this.delegateReduction = delegateReduction;
	}
	public String getDelegateTotalValue() {
		return delegateTotalValue;
	}
	public void setDelegateTotalValue(String delegateTotalValue) {
		this.delegateTotalValue = delegateTotalValue;
	}
    
    
}
