package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class DelegationListByStakingResp {

	private String delegateAddr; // 委托人地址
	private String delegateValue; // 委托金额
	private String delegateTotalValue;// 验证人委托的总金额
	private String delegateLocked;    //已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）
	private String allDelegateLocked; //当前验证人总接收的锁定委托量（LAT）  staking  stat_delegate_locked
}
