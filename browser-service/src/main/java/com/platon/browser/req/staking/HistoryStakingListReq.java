package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

/**
 * 历史验证人查询验证对象
 *  @file HistoryStakingListReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class HistoryStakingListReq extends PageReq{
    private String key;
}