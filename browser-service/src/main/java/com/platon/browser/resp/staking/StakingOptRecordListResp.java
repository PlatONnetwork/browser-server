package com.platon.browser.resp.staking;

import lombok.Data;

/**
 * 验证人操作列表返回对象
 *  @file StakingOptRecordListResp.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Data
public class StakingOptRecordListResp {
	private Long timestamp;         //创建时间
    private String desc;              //操作描述
    private String txHash;            //所属交易
    private Long blockNumber;       //所属区块
}
