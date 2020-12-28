package com.platon.browser.response.staking;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 历史验证人列表返回对象
 *  @file HistoryStakingListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class HistoryStakingListResp {
	private String nodeId;            //出块节点地址
	private String nodeName;          //验证人名称
	private String stakingIcon;       //验证人图标
	private Integer status;            //状态 4:退出中 5:已退出
	private BigDecimal statDelegateReduction; //待提取的委托
	private Integer slashLowQty;       //低出块率举报次数
	private Integer slashMultiQty;     //多签举报次数
	private Long leaveTime;      //退出时间
	private Long blockQty;          //产生的区块数
	private Long unlockBlockNum; //预估解锁块高
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
	public String getStakingIcon() {
		return stakingIcon;
	}
	public void setStakingIcon(String stakingIcon) {
		this.stakingIcon = stakingIcon;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public BigDecimal getStatDelegateReduction() {
		return statDelegateReduction;
	}
	public void setStatDelegateReduction(BigDecimal statDelegateReduction) {
		this.statDelegateReduction = statDelegateReduction;
	}
	public Integer getSlashLowQty() {
		return slashLowQty;
	}
	public void setSlashLowQty(Integer slashLowQty) {
		this.slashLowQty = slashLowQty;
	}
	public Integer getSlashMultiQty() {
		return slashMultiQty;
	}
	public void setSlashMultiQty(Integer slashMultiQty) {
		this.slashMultiQty = slashMultiQty;
	}
	public Long getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Long getBlockQty() {
		return blockQty;
	}
	public void setBlockQty(Long blockQty) {
		this.blockQty = blockQty;
	}

	public Long getUnlockBlockNum() {
		return unlockBlockNum;
	}

	public void setUnlockBlockNum(Long unlockBlockNum) {
		this.unlockBlockNum = unlockBlockNum;
	}
}
