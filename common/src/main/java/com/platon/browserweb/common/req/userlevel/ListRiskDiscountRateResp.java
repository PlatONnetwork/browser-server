package com.platon.browserweb.common.req.userlevel;

import java.util.Date;

public class ListRiskDiscountRateResp {	
    /**
     * 风控折扣id
     */
    private Long id;
	/**
	 * 等级代码
	 */
	private String levelCode;
	/**
	 * 业务类型
WITHDRAW：提币
TRADE：交易
	 */
	private String bizCode;

    /**
     * 每笔交易最大阈值（基准货币)
     */
    private String maxQty;

    /**
     * 单次交易人工审核阀值（基准货币）
     */
    private String maxConfirmQty;

    /**
     * 每笔交易最大阈值（计价货币)
     */
    private String maxAmount;

    /**
     * 单次交易人工审核阀值（计价货币）
     */
    private String maxConfirmAmount;

    /**
     * 每天交易最大阈值折扣率 百分比，配置成小数。
如果20%，配置成0.20（WITHDRAW）
     */
    private String dailyQty;
    
    /**
     * 每天交易最大阈值（计价货币）
     */
    private String dailyAmount;

    /**
     * 每天交易最大次数
     */
    private String maxCount;
    
    /**
     * juweb用户id
     */
    private Integer juserId;

    /**
     * juweb用户id
     */
    private String juserName;

    /**
     * 设置时间
     */
    private Date setTime;

	/**
	 * 失效时间
	 */
    private Date disabledTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
	}

	public String getMaxConfirmQty() {
		return maxConfirmQty;
	}

	public void setMaxConfirmQty(String maxConfirmQty) {
		this.maxConfirmQty = maxConfirmQty;
	}

	public String getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(String maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getMaxConfirmAmount() {
		return maxConfirmAmount;
	}

	public void setMaxConfirmAmount(String maxConfirmAmount) {
		this.maxConfirmAmount = maxConfirmAmount;
	}

	public String getDailyQty() {
		return dailyQty;
	}

	public void setDailyQty(String dailyQty) {
		this.dailyQty = dailyQty;
	}

	public String getDailyAmount() {
		return dailyAmount;
	}

	public void setDailyAmount(String dailyAmount) {
		this.dailyAmount = dailyAmount;
	}

	public String getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(String maxCount) {
		this.maxCount = maxCount;
	}

	public Integer getJuserId() {
		return juserId;
	}

	public void setJuserId(Integer juserId) {
		this.juserId = juserId;
	}

	public String getJuserName() {
		return juserName;
	}

	public void setJuserName(String juserName) {
		this.juserName = juserName;
	}

	public Date getSetTime() {
		return setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	public Date getDisabledTime() {
		return disabledTime;
	}

	public void setDisabledTime(Date disabledTime) {
		this.disabledTime = disabledTime;
	}
}
