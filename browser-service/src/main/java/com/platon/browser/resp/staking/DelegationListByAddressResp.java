package com.platon.browser.resp.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class DelegationListByAddressResp {
	private String nodeId;            //节点id
    private String nodeName;          //节点名称
    private String delegateValue;     //委托数量
    private String delegateHas;       //未锁定委托（LAT）
    private String delegateLocked;    //已锁定委托（LAT）
    private String allDelegateLocked; //当前验证人总接收的锁定委托量（LAT）
    private String delegateUnlock;    //已解除委托（LAT） 
    private String delegateReduction;  //赎回中委托（LAT） 
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
    
    
}
