package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class TransactionInfo {
    private String txHash;

    private String from;

    private String to;

    private double value;

    private int blockHeight;

    private int transactionIndex;

    private long timestamp;
}
