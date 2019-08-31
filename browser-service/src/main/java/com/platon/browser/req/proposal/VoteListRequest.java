package com.platon.browser.req.proposal;

import lombok.Data;

import com.platon.browser.req.PageReq;

/**
 * 投票请求对象
 *  @file VoteListRequest.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class VoteListRequest extends PageReq{
    private String proposalHash;
    
    private String option;
}