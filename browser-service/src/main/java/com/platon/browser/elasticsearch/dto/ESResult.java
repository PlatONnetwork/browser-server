package com.platon.browser.elasticsearch.dto;

import java.util.List;

import lombok.Data;

@Data
public class ESResult<T> {

	private Long total;
	
	private List<T> rsData;

}
