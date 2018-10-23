package com.platon.browserweb.common.req.currency;

public class SetQuoteCurrencyReq {
	
	private String quoteCurrency;
	
	private Boolean enable;

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
