package com.platon.browser.request.newblock;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 区块详情切换对象
 *  @file BlockDetailNavigateReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public class BlockDetailNavigateReq {
    @NotBlank(message = "{navigate.direction.notnull}")
    @Pattern(regexp = "prev|next", message = "{direction.illegal}")
    private String direction;
    @NotNull(message = "{block.number.notnull}")
    private Long number;
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
}
