package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class PendingTxDetail {
    private String txHash;
    private long timestamp;
    private int txReceiptStatus;
    private String blockHeight;
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
}
