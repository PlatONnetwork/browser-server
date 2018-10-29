package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class TransactionList {
    private String txHash;
    private Long blockHeight;
    private long blockTime;
    private String from;
    private String to;
    private String value;
    private String actualTxCost;
    private int txReceiptStatus;
    private String txType;
    private long serverTime;
    private String failReason;
}
