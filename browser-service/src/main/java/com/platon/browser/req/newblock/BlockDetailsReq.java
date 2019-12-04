package com.platon.browser.req.newblock;

import javax.validation.constraints.NotNull;

/**
 * 区块详情查询请求对象
 *  @file BlockDetailsReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockDetailsReq {
    @NotNull(message = "{number not null}")
    private Integer number;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
    
}