package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class AliveStakingListResp {

	private Integer ranking;           //排行
	private String nodeId;            //节点id
	private String nodeName;          //验证人名称
	private String stakingIcon;       //验证人图标
	private Integer status;            //状态   1:候选中  2:活跃中  3:出块中
	private String totalValue;        //质押总数=有效的质押+委托
	private String delegateValue;     //委托总数
	private Integer delegateQty;       //委托人数
	private Integer slashLowQty;      //低出块率举报次数
	private Integer slashMultiQty;    //多签举报次数
	private Long blockQty;          //产生的区块数
	private String expectedIncome;    //预计年收化率（从验证人加入时刻开始计算）
	private Boolean isRecommend;     //是否官方推荐
	private Boolean isInit;          //是否为初始节点 
}
