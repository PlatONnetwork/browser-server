package com.platon.browserweb.common.req.tradezone;

import com.platon.browserweb.common.req.PageReq;
import org.hibernate.validator.constraints.NotEmpty;

public class ListTradeZoneSetReq extends PageReq {
	
	@NotEmpty(message="quoteCurrency不能为空！")
	private String quoteCurrency;
	
	private String status;
	
	private String category;

	private Boolean enable;

	public String getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(String quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
