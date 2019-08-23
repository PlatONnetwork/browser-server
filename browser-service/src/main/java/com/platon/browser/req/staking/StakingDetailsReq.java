package com.platon.browser.req.staking;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class StakingDetailsReq {
    @NotBlank(message = "{nodeId not null}")
    private String nodeId;
}