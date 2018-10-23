package com.platon.browserweb.common.req.risk;

import java.util.Date;

import com.platon.browserweb.common.req.PageReq;

public class ListDepositRiskHistoryReq extends PageReq{
	
    /**
     * 币种代码
     */
    private String currency;
    
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
     * 用户类型
     * @return
     */
    private String userType;

	/**
	 * 代理商ID
	 */
    private Long brokerId;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
}
