package com.platon.browser.res.home;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.CustomLatSerializer;

public class StakingListResp {
	private String nodeId; // 出块节点Id
	private String nodeName; // 出块节点名称
	private String stakingIcon; // 验证人图片
	private Integer ranking; // 节点排行
	private String expectedIncome; // 预计年收化率（从验证人加入时刻开始计算）
	private Boolean isInit; // 是否为初始化的验证人，如果是expectedIncome不显示数值
	private String totalValue; //
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
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	public String getExpectedIncome() {
		return expectedIncome;
	}
	public void setExpectedIncome(String expectedIncome) {
		this.expectedIncome = expectedIncome;
	}
	public Boolean getIsInit() {
		return isInit;
	}
	public void setIsInit(Boolean isInit) {
		this.isInit = isInit;
	}
	@JsonSerialize(using = CustomLatSerializer.class)
	public String getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	
}
