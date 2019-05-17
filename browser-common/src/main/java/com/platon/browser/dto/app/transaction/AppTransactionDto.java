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
    private Long blockNumber;
    private String chainId;
    private String from;
    private String hash;
    private Long sequence;
    private Date timestamp;
    private String to;
    private Integer transactionIndex;
    private String txInfo;
    private Integer txReceiptStatus;
    private String txType;
    private String value;
}
