package com.platon.browserweb.common.req.tradezone;

public class SetTradeZoneStatusReq {
	
	private String symbol;

	private Boolean enable;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
