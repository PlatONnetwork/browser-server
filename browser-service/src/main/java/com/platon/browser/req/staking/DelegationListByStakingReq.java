package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class DelegationListByStakingReq extends PageReq{
    private String nodeId;
    private String stakingBlockNum;
}