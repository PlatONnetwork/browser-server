package com.platon.browserweb.common.req.userlevel;

import java.util.Date;

public class ListFeeDiscountRateResp {
    /**
     * 费率id
     */
    private Long id;

    /**
     * 等级代码
default：默认设置
     */
    private String levelCode;

    /**
     * 提币折扣率 百分比，配置成小数。
如果20%，配置成0.20
     */
    private String withdraw;

    /**
     * 交易折扣率 百分比，配置成小数。
如果20%，配置成0.20
     */
    private String trade;

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

	public String getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
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
