package com.platon.browser.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TransactionItem {
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
    // to字段存储的账户类型：account-钱包地址，contract-合约地址
    private String receiveType;

    @JsonIgnore
    private long timestamp;
}
