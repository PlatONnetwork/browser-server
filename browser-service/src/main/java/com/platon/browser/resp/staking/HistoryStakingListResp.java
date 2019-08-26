package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class HistoryStakingListResp {
	private String nodeId;            //出块节点地址
	private String nodeName;          //验证人名称
	private String stakingIcon;       //验证人图标
	private Integer status;            //状态 4:退出中 5:已退出
	private String statDelegateReduction; //待提取的委托
	private Integer slashLowQty;       //低出块率举报次数
	private Integer slashMultiQty;     //多签举报次数
	private Long leaveTime;      //退出时间
	private Long blockQty;          //产生的区块数
}
