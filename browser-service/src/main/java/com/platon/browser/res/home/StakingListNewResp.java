package com.platon.browser.res.home;

import lombok.Data;

@Data
public class StakingListNewResp {
	private String nodeId; // 出块节点Id
	private String nodeName; // 出块节点名称
	private String stakingIcon; // 验证人图片
	private Integer ranking; // 节点排行
	private String expectedIncome; // 预计年收化率（从验证人加入时刻开始计算）
	private Boolean isInit; // 是否为初始化的验证人，如果是expectedIncome不显示数值
}
