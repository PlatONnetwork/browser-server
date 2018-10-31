package com.platon.browser.dto.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class AccTransactionDetail {
    private String chainId;
    private String txHash;
    private Date blockTime;
    private String from;
    private String to;
    private String value;
    private Integer energonUsed;
    private Integer energonLimit;
    private String energonPrice;
    private Long transactionIndex;
    private String actualTxCost;
    private Long txReceiptStatus;
    private String txType;
    private Date serverTime;
    private Date createTime;
    private Date updateTime;
}
