package com.platon.browser.resp.staking;

import lombok.Data;

@Data
public class StakingOptRecordListResp {
	private Long timestamp;         //创建时间
    private String desc;              //操作描述
    private String txHash;            //所属交易
    private Long blockNumber;       //所属区块
}
