package com.platon.browser.response.transaction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 交易列表返回对象
 *  @file TransactionListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class TransactionListResp {
	private String txHash;    //交易hash
    private String from;      //发送方地址（操作地址）
    private String to;        //接收方地址
    private Long seq;        //排序号
    private BigDecimal value;          //金额(单位:von)
    private BigDecimal actualTxCost;    //交易费用(单位:von)
    private String txType;             //交易类型 0：转账  1：合约发布  2：合约调用    5：MPC交易
    private Long serverTime;    //服务器时间
    private Long timestamp;//出块时间
    private Long blockNumber;  //交易所在区块高度
    private String failReason;        //失败原因
    private String receiveType; //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
                                    //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
    private Integer txReceiptStatus;     //交易状态
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getActualTxCost() {
		return actualTxCost;
	}
	public void setActualTxCost(BigDecimal actualTxCost) {
		this.actualTxCost = actualTxCost;
	}
	public String getTxType() {
		return txType;
	}
	public void setTxType(String txType) {
		this.txType = txType;
	}
	public Long getServerTime() {
		return serverTime;
	}
	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getReceiveType() {
		return receiveType;
	}
	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}
	public Integer getTxReceiptStatus() {
		return txReceiptStatus;
	}
	public void setTxReceiptStatus(Integer txReceiptStatus) {
		this.txReceiptStatus = txReceiptStatus;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}
}
