package com.platon.browser.service.elasticsearch.bean;

import java.util.List;

public class ESResult<T> {

	private Long total;
	
	private List<T> rsData;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRsData() {
		return rsData;
	}

	public void setRsData(List<T> rsData) {
		this.rsData = rsData;
	}
	
}
