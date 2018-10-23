package com.platon.browserweb.common.req.risk;

import java.util.Date;

import com.platon.browserweb.common.base.Dto;

public class ListTradeRiskResp extends Dto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3309528945445646722L;

	/**
     * id
     */
    private Long id;

    /**
     * 货币对
     */
    private String symbol;

    /**
     * 每笔交易最小阈值（基准货币)
     */
    private String minQty;

    /**
     * 每笔交易最大阈值（基准货币)
     */
    private String maxQty;

    /**
     * 单次交易人工审核阀值（基准货币）
     */
    private String maxConfirmQty;

    /**
     * 每笔交易最小阈值（计价货币)
     */
    private String minAmount;

    /**
     * 每笔交易最大阈值（计价货币)
     */
    private String maxAmount;

    /**
     * 单次交易人工审核阀值（计价货币）
     */
    private String maxConfirmAmount;

    /**
     * 每天交易最大阈值（计价货币）
     */
    private String dailyAmount;

    /**
     * 每天交易最大次数
     */
    private Integer maxCount;

    /**
     * juweb用户id
     */
    private Integer juserId;

    /**
     * juweb用户姓名
     */
    private String juserName;

    /**
     * 设置时间
     */
    private Date setTime;

	/**
	 * 是否启用
	 */
    private Boolean enable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getMinQty() {
		return minQty;
	}

	public void setMinQty(String minQty) {
		this.minQty = minQty;
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

	public String getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(String minAmount) {
		this.minAmount = minAmount;
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

	public String getDailyAmount() {
		return dailyAmount;
	}

	public void setDailyAmount(String dailyAmount) {
		this.dailyAmount = dailyAmount;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
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

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
