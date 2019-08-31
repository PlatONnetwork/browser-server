package com.platon.browser.req.newblock;

import lombok.Data;

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
@Data
public class BlockDetailNavigateReq {
    @NotBlank(message = "{navigate.direction.notnull}")
    @Pattern(regexp = "prev|next", message = "{direction.illegal}")
    private String direction;
    @NotNull(message = "{block.number.notnull}")
    private Long number;
}
