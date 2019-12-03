package com.platon.browser.res.address;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

import java.math.BigDecimal;

/**
 *  查询地址的返回的对象
 *  @file QueryDetailResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class QueryDetailResp {
	private Integer type;                //地址详情  1：账号   2：合约   3：内置合约
    private BigDecimal balance;             //余额(单位:LAT)
    private BigDecimal restrictingBalance;  //锁仓余额(单位:LAT)
    private BigDecimal stakingValue;        //质押的金额
    private BigDecimal delegateValue;       //委托的金额
    private BigDecimal redeemedValue;       //赎回中的金额
    private Integer txQty;             //交易总数
    private Integer transferQty;         //转账交易总数
    private Integer delegateQty;         //委托交易总数
    private Integer stakingQty;          //验证人交易总数
    private Integer proposalQty;         //治理交易总数
    private Integer candidateCount;      //已委托验证人
    private BigDecimal delegateHes;         //未锁定委托（LAT）
    private BigDecimal delegateLocked;      //已锁定委托（LAT）
    private BigDecimal delegateUnlock;      //已解除委托（LAT）   
    private BigDecimal delegateReleased;    //待赎回委托（LAT）   
    private String contractName;        //合约名称
    private String contractCreate;      //合约创建者地址
    private String contractCreateHash; //合约创建哈希
    private Integer isRestricting; //是否锁仓
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getRestrictingBalance() {
		return restrictingBalance;
	}
	public void setRestrictingBalance(BigDecimal restrictingBalance) {
		this.restrictingBalance = restrictingBalance;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getStakingValue() {
		return stakingValue;
	}
	public void setStakingValue(BigDecimal stakingValue) {
		this.stakingValue = stakingValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegateValue() {
		return delegateValue;
	}
	public void setDelegateValue(BigDecimal delegateValue) {
		this.delegateValue = delegateValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getRedeemedValue() {
		return redeemedValue;
	}
	public void setRedeemedValue(BigDecimal redeemedValue) {
		this.redeemedValue = redeemedValue;
	}
	public Integer getTxQty() {
		return txQty;
	}
	public void setTxQty(Integer txQty) {
		this.txQty = txQty;
	}
	public Integer getTransferQty() {
		return transferQty;
	}
	public void setTransferQty(Integer transferQty) {
		this.transferQty = transferQty;
	}
	public Integer getDelegateQty() {
		return delegateQty;
	}
	public void setDelegateQty(Integer delegateQty) {
		this.delegateQty = delegateQty;
	}
	public Integer getStakingQty() {
		return stakingQty;
	}
	public void setStakingQty(Integer stakingQty) {
		this.stakingQty = stakingQty;
	}
	public Integer getProposalQty() {
		return proposalQty;
	}
	public void setProposalQty(Integer proposalQty) {
		this.proposalQty = proposalQty;
	}
	public Integer getCandidateCount() {
		return candidateCount;
	}
	public void setCandidateCount(Integer candidateCount) {
		this.candidateCount = candidateCount;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegateHes() {
		return delegateHes;
	}
	public void setDelegateHes(BigDecimal delegateHes) {
		this.delegateHes = delegateHes;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegateLocked() {
		return delegateLocked;
	}
	public void setDelegateLocked(BigDecimal delegateLocked) {
		this.delegateLocked = delegateLocked;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegateUnlock() {
		return delegateUnlock;
	}
	public void setDelegateUnlock(BigDecimal delegateUnlock) {
		this.delegateUnlock = delegateUnlock;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getDelegateReleased() {
		return delegateReleased;
	}
	public void setDelegateReleased(BigDecimal delegateReleased) {
		this.delegateReleased = delegateReleased;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getContractCreate() {
		return contractCreate;
	}
	public void setContractCreate(String contractCreate) {
		this.contractCreate = contractCreate;
	}
	public String getContractCreateHash() {
		return contractCreateHash;
	}
	public void setContractCreateHash(String contractCreateHash) {
		this.contractCreateHash = contractCreateHash;
	}
	public Integer getIsRestricting() {
		return isRestricting;
	}
	public void setIsRestricting(Integer isRestricting) {
		this.isRestricting = isRestricting;
	}
    
}
