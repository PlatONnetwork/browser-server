package com.platon.browser.res.staking;

/**
 * 锁定验证人列表返回对象
 *  @file AliveStakingListResp.javaStakingListResp
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class LockedStakingListResp extends AliveStakingListResp {
	private Long leaveTime;      //退出时间
	public Long getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
}
