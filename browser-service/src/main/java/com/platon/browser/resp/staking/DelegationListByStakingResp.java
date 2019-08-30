package com.platon.browser.resp.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class DelegationListByStakingResp {

	private String delegateAddr; // 委托人地址
	private String delegateValue; // 委托金额
	private String delegateTotalValue;// 验证人委托的总金额
	private String delegateLocked;    //已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）
	private String allDelegateLocked; //当前验证人总接收的锁定委托量（LAT）  staking  stat_delegate_locked
	public String getDelegateAddr() {
		return delegateAddr;
	}
	public void setDelegateAddr(String delegateAddr) {
		this.delegateAddr = delegateAddr;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateValue() {
		return delegateValue;
	}
	public void setDelegateValue(String delegateValue) {
		this.delegateValue = delegateValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateTotalValue() {
		return delegateTotalValue;
	}
	public void setDelegateTotalValue(String delegateTotalValue) {
		this.delegateTotalValue = delegateTotalValue;
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
	
	
}
