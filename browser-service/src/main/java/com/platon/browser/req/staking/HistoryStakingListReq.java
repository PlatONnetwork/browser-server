package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class HistoryStakingListReq extends PageReq{
    private String nodeId;
}