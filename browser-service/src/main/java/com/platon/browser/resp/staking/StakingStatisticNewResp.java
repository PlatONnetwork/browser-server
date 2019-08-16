package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class StakingStatisticNewResp {

	private String stakingDelegationValue; // 质押委托总数
	private String stakingValue; // 质押总数
	private String issueValue; // 发行量
	private String blockReward; // 当前的出块奖励
	private String stakingReward; // 当前的质押奖励
	private Long currentNumber; // 当前区块高度
	private Long addIssueBegin; // 当前增发周期的开始快高
	private Long addIssueEnd; // 当前增发周期的结束块高
	private Long nextSetting; // 离下个结算周期倒计时
}
