package com.platon.browser.req.staking;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.platon.browser.req.PageReq;

@Data
public class AliveStakingListReq extends PageReq{
    private String key;
    @NotBlank(message = "{queryStatus not null}")
    @Pattern(regexp = "all|active|candidate", message = "{queryStatus.illegal}")
    private String queryStatus;
}