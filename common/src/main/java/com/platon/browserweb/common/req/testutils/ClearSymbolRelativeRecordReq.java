package com.platon.browserweb.common.req.testutils;

import org.hibernate.validator.constraints.NotEmpty;

public class ClearSymbolRelativeRecordReq {
	@NotEmpty(message="币对参数（symbol）不能为空！")
	private String symbol;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
