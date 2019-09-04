package com.platon.browser.dao.entity;

import java.util.Date;

public class DelegationStaking extends DelegationKey {
	private String delegateHas;

    private String delegateLocked;

    private String delegateReduction;

    private Integer isHistory;

    private Long sequence;

    private Date createTime;

    private Date updateTime;
    
    private String statDelegateHas;
    private String statDelegateLocked;
    private Integer status;
    private String stakingName;
    private String statDelegateReduction;
    
    private String allDelegate;
    private String allLockDelegate;
    
	public String getDelegateHas() {
		return delegateHas;
	}
	public void setDelegateHas(String delegateHas) {
		this.delegateHas = delegateHas;
	}
	public String getDelegateLocked() {
		return delegateLocked;
	}
	public void setDelegateLocked(String delegateLocked) {
		this.delegateLocked = delegateLocked;
	}
	public String getDelegateReduction() {
		return delegateReduction;
	}
	public void setDelegateReduction(String delegateReduction) {
		this.delegateReduction = delegateReduction;
	}
	public Integer getIsHistory() {
		return isHistory;
	}
	public void setIsHistory(Integer isHistory) {
		this.isHistory = isHistory;
	}
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getStatDelegateHas() {
		return statDelegateHas;
	}
	public void setStatDelegateHas(String statDelegateHas) {
		this.statDelegateHas = statDelegateHas;
	}
	public String getStatDelegateLocked() {
		return statDelegateLocked;
	}
	public void setStatDelegateLocked(String statDelegateLocked) {
		this.statDelegateLocked = statDelegateLocked;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStakingName() {
		return stakingName;
	}
	public void setStakingName(String stakingName) {
		this.stakingName = stakingName;
	}
	public String getStatDelegateReduction() {
		return statDelegateReduction;
	}
	public void setStatDelegateReduction(String statDelegateReduction) {
		this.statDelegateReduction = statDelegateReduction;
	}
	public String getAllDelegate() {
		return allDelegate;
	}
	public void setAllDelegate(String allDelegate) {
		this.allDelegate = allDelegate;
	}
	public String getAllLockDelegate() {
		return allLockDelegate;
	}
	public void setAllLockDelegate(String allLockDelegate) {
		this.allLockDelegate = allLockDelegate;
	}
    
    
}
