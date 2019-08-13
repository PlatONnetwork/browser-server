package com.platon.browser.res.transaction;

import lombok.Data;

@Data
public class TransactionDetailsRPPlanResp {
	private Integer epoch;         //锁仓周期
    private Long amount;      //锁定金额
    private Long blockNumber;   //锁仓周期对应快高  结束周期 * epoch  
}
