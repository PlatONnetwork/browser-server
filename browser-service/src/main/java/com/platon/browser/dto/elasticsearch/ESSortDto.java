package com.platon.browser.dto.elasticsearch;

import org.elasticsearch.search.sort.SortOrder;

import lombok.Data;

/**
 * es传输排序dto
 *  @file ESSortDto.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年10月31日
 */
@Data
public class ESSortDto {

	private String sortName;
	
	private SortOrder sortOrder;
	
	public ESSortDto(String sortName, SortOrder sortOrder) {
		this.sortName = sortName;
		this.sortOrder = sortOrder;
	}
}
