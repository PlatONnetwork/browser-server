package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class TransactionDetail {
    private String txHash;
    private long timestamp;
    private int txReceiptStatus;
    private Long blockHeight;
    private String from;
    private String to;
    private String txType;
    private String value;
    private String actualTxCost;
    private int energonLimit;
    private int energonUsed;
    private String energonPrice;
    private String inputData;
    private long expectTime;
    private String failReason;
}
