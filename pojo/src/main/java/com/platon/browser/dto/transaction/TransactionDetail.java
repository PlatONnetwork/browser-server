package com.platon.browser.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String energonLimit;
    private String energonUsed;
    private String energonPrice;
    private String inputData;
    private long expectTime;
    private String failReason;
    private long confirmNum;
    private String receiveType;
    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
    @JsonIgnore
    private Long sequence;
}
