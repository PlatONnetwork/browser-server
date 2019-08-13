package com.platon.browser.req.proposal;

import lombok.Data;

import com.platon.browser.req.PageReq;

@Data
public class VoteListRequest extends PageReq{
    private String proposalHash;
    
    private String option;
}