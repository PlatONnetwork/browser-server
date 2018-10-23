package com.platon.browserweb.common.req.fee;

import java.util.List;

public class ListTradeFeeReq {
	//类型1：执行中2：待执行3：已过期 
	private int type;
	//计价货币
	private String quoteCurrency;

	// 代理商ID
	private Long brokerId;

	// 货币对列表
	private List<String> symbolList;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}

	public List<String> getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(List<String> symbolList) {
		this.symbolList = symbolList;
	}
}
