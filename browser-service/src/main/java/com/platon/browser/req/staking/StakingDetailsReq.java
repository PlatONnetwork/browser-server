package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 验证人详情请求对象
 *  @file StakingDetailsReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class StakingDetailsReq {
    @NotBlank(message = "{nodeId not null}")
    private String nodeId;
}