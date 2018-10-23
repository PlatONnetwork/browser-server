package com.platon.browserweb.common.req.userlevel;

import java.util.Date;

import com.platon.browserweb.common.req.PageReq;

public class ListFeeDiscountRateHistoryReq extends PageReq {

	/**
	 * 等级代码
	 */
	private String levelCode;
    /**
     * 操作者姓名
     */
    private String optName;
    
    /**
     * 始（用户创建时间）
     */
    private Date startTime;
    
    /**
     * 终（用户创建时间）
     */
    private Date endTime;

	/**
	 * 代理商ID
	 */
    private Long brokerId;

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
}
