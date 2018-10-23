package com.platon.browserweb.common.req.operatinglog;

import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.validate.UserIdGroup;

import javax.validation.constraints.NotNull;

public class OperatingLogReq extends PageReq{

	/**
	 * 用户id
	 */
	@NotNull(groups = UserIdGroup.class)
	private long userId;

	private String optName;

	private String type;

	private long noticeId;
	
	private String currency;
	
	private String symbol;

	private String quoteCurrency;

	public String getOptName() {
		return optName;
	}

	public void setOptName(String optName) {
		this.optName = optName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUserId () {
		return userId;
	}

	public void setUserId ( long userId ) {
		this.userId = userId;
	}

	public long getNoticeId () {
		return noticeId;
	}

	public void setNoticeId ( long noticeId ) {
		this.noticeId = noticeId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}
}
