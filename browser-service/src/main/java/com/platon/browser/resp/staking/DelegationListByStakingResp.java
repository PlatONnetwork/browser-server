package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class DelegationListByStakingResp {

	private String delegateAddr; // 委托人地址
	private String delegateValue; // 委托金额
	private String delegateTotalValue;// 验证人委托的总金额
	private String locketValue; // 锁定的委托金额
	private String locketTotalValue; // 验证人锁定的委托总额
}
