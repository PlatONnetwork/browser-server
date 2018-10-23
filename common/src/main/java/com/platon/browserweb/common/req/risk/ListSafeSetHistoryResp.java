package com.platon.browserweb.common.req.risk;

import java.util.Date;

public class ListSafeSetHistoryResp {
    /**
     * juweb用户id
     */
    private Integer createJuserId;

    /**
     * juweb用户姓名
     */
    private String createJuserName;

    /**
     * setTime时间
     */
    private Date createTime;
    
    /**
     * 配置值
     */
    private String comment;

	/**
	 * 失效时间
	 */
    private Date disabledTime;

	/**
	 * 委托买价阈值
	 */
    private String entrustBuyQty;

	/**
	 * 委托卖价阈值
	 */
    private String entrustSellQty;

	public Integer getCreateJuserId() {
		return createJuserId;
	}

	public void setCreateJuserId(Integer createJuserId) {
		this.createJuserId = createJuserId;
	}

	public String getCreateJuserName() {
		return createJuserName;
	}

	public void setCreateJuserName(String createJuserName) {
		this.createJuserName = createJuserName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDisabledTime() {
		return disabledTime;
	}

	public void setDisabledTime(Date disabledTime) {
		this.disabledTime = disabledTime;
	}

	public String getEntrustBuyQty() {
		return entrustBuyQty;
	}

	public void setEntrustBuyQty(String entrustBuyQty) {
		this.entrustBuyQty = entrustBuyQty;
	}

	public String getEntrustSellQty() {
		return entrustSellQty;
	}

	public void setEntrustSellQty(String entrustSellQty) {
		this.entrustSellQty = entrustSellQty;
	}
}
