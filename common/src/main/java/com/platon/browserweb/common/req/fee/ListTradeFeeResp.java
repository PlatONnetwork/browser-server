package com.platon.browserweb.common.req.fee;

import java.util.Date;

import com.platon.browserweb.common.base.Dto;

public class ListTradeFeeResp extends Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 250710599353198658L;

	/**
     * id
     */
    private Long id;

    /**
     * 币种 （如果为空，默认设置）
     */
    private String currency;
    
    /**
     * 币对 
     */
    private String symbol;

    /**
     * 代理商手续费配置
     */
    private String brokerFee;
    
    /**
     * 投资者手续费配置
     */
    private String investorFee;
    
    /**
     * 生效时间（前端精确到时间）
     */
    private Date effectiveTime;

    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 设置时间
     */
    private Date setTime;  

    /**
     * juweb用户id
     */
    private Integer juserId;

    /**
     * juweb用户姓名
     */
    private String juserName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBrokerFee() {
		return brokerFee;
	}

	public void setBrokerFee(String brokerFee) {
		this.brokerFee = brokerFee;
	}

	public String getInvestorFee() {
		return investorFee;
	}

	public void setInvestorFee(String investorFee) {
		this.investorFee = investorFee;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getSetTime() {
		return setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
