package com.platon.browser.req.staking;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.platon.browser.req.PageReq;

/**
 *  活跃验证人列表请求对象
 *  @file AliveStakingListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class AliveStakingListReq extends PageReq{
    private String key;
    @NotBlank(message = "{queryStatus not null}")
    @Pattern(regexp = "all|active|candidate", message = "{queryStatus.illegal}")
    private String queryStatus;
}