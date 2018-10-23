package com.platon.browserweb.common.req.risk;

import java.util.Date;

import com.platon.browserweb.common.base.Dto;

public class ListDepositRiskResp extends Dto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5613113529188655680L;

	/**
     * id
     */
    private Long id;
    
    /**
     * 币种代码
     */
    private String currency;

    /**
     * 币种图标
     */
    private String currencyIcon;
    
    /**
     * 每笔交易最小阈值
     */
    private String minQty;
    
    /**
     * juweb用户id
     */
    private Integer juserId;

    /**
     * juweb用户姓名
     */
    private String juserName;

    /**
     * setTime时间
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencyIcon() {
		return currencyIcon;
	}

	public void setCurrencyIcon(String currencyIcon) {
		this.currencyIcon = currencyIcon;
	}

	public String getMinQty() {
		return minQty;
	}

	public void setMinQty(String minQty) {
		this.minQty = minQty;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
