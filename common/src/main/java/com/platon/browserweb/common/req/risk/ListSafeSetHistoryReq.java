package com.platon.browserweb.common.req.risk;

import java.util.Date;

import com.platon.browserweb.common.req.PageReq;

public class ListSafeSetHistoryReq extends PageReq {
    
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
     * 配置类型
     * @return
     */
    private String configType;
    
    /**
     * 配置代码
     * @return
     */
    private String configCode;

	/**
	 * 代理商ID
	 */
	private Long userId;

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

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
