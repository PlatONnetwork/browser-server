package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class AliveStakingListReq extends PageReq{
    private String key;
    
    private String queryStatus;
}