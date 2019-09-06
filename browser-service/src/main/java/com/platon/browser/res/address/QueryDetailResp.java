package com.platon.browser.res.address;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 *  查询地址的返回的对象
 *  @file QueryDetailResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class QueryDetailResp {
	private Integer type;                //地址详情  1：账号   2：合约   3：内置合约
    private String balance;             //余额(单位:LAT)
    private String restrictingBalance;  //锁仓余额(单位:LAT)
    private String stakingValue;        //质押的金额
    private String delegateValue;       //委托的金额
    private String redeemedValue;       //赎回中的金额
    private Integer txQty;             //交易总数
    private Integer transferQty;         //转账交易总数
    private Integer delegateQty;         //委托交易总数
    private Integer stakingQty;          //验证人交易总数
    private Integer proposalQty;         //治理交易总数
    private Integer candidateCount;      //已委托验证人
    private String delegateHes;         //未锁定委托（LAT）
    private String delegateLocked;      //已锁定委托（LAT）
    private String delegateUnlock;      //已解除委托（LAT）   
    private String delegateReduction;    //赎回中委托（LAT）   
    private String contractName;        //合约名称
    private String contractCreate;      //合约创建者地址
    private String contractCreateHash; //合约创建哈希
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getRestrictingBalance() {
		return restrictingBalance;
	}
	public void setRestrictingBalance(String restrictingBalance) {
		this.restrictingBalance = restrictingBalance;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getStakingValue() {
		return stakingValue;
	}
	public void setStakingValue(String stakingValue) {
		this.stakingValue = stakingValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateValue() {
		return delegateValue;
	}
	public void setDelegateValue(String delegateValue) {
		this.delegateValue = delegateValue;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getRedeemedValue() {
		return redeemedValue;
	}
	public void setRedeemedValue(String redeemedValue) {
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
	public String getDelegateHes() {
		return delegateHes;
	}
	public void setDelegateHes(String delegateHes) {
		this.delegateHes = delegateHes;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getDelegateLocked() {
		return delegateLocked;
	}
	public void setDelegateLocked(String delegateLocked) {
		this.delegateLocked = delegateLocked;
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
    
}
