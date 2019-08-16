package com.platon.browser.req.newblock;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class BlockListByNodeIdReq extends PageReq{
    private String nodeId;
}