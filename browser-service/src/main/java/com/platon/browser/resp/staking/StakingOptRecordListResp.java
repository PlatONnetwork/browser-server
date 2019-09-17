package com.platon.browser.resp.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

/**
 * 验证人操作列表返回对象
 *  @file StakingOptRecordListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class StakingOptRecordListResp {
	private Long timestamp;         //创建时间
    private String desc;              //操作描述
    private String txHash;            //所属交易
    private Long blockNumber;       //所属区块
    private String type; //类型
    private String id;  //提案id
    private String title;  //提案标题
    private String option;  //提案选项
    private String percent;  //处罚百分比
    private String amount;  //处罚金额
    private Integer isFire;  //是否踢出列表  0-否，1-是
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Integer getIsFire() {
		return isFire;
	}
	public void setIsFire(Integer isFire) {
		this.isFire = isFire;
	}
    
}
