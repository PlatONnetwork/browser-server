package com.platon.browser.req.staking;

import lombok.Data;

import com.platon.browser.req.PageReq;

/**
 * 验证人委托列表请求对象
 *  @file DelegationListByStakingReq.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class DelegationListByStakingReq extends PageReq{
    private String nodeId;
    private String stakingBlockNum;
}