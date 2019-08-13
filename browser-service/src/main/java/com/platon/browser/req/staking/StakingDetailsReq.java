package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

import com.platon.browser.req.PageReq;

import lombok.Data;

@Data
public class StakingDetailsReq extends PageReq{
    @NotBlank(message = "{nodeId not null}")
    private String nodeId;
}