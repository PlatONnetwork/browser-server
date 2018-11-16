package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class TransactionInfo {
    private String txHash;
    private String from;
    private String to;
    private String value;
    private long blockHeight;
    private int transactionIndex;
    private long timestamp;
    private String txType;
    private String receiveType;
}
