package com.platon.browserweb.common.req.currency;

import com.platon.browserweb.common.req.PageReq;

public class ListCurrencyReq extends PageReq{
	
	private Boolean queryStatus;

	public Boolean getQueryStatus() {
		return queryStatus;
	}

	public void setQueryStatus(Boolean queryStatus) {
		this.queryStatus = queryStatus;
	}
	
}
