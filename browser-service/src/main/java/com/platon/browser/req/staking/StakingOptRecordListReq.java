package com.platon.browser.req.staking;

import lombok.Data;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

@Data
public class StakingOptRecordListReq extends PageReq{
	@NotBlank(message = "{nodeId not null}")
    private String nodeId;
}