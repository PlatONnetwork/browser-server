package com.platon.browser.dto.app.transaction;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class AppTransactionDto {
    private String actualTxCost;
    private String blockNumber;
    private String chainId;
    private String from;
    private String hash;
    private String sequence;
    private String timestamp;
    private String to;
    private String transactionIndex;
    private String txInfo;
    private String txReceiptStatus;
    private String txType;
    private String value;
}
