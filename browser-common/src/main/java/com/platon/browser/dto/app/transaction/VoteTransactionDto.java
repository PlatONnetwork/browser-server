package com.platon.browser.dto.app.transaction;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class VoteTransactionDto {
    private String hash;
    private String nodeId;
    private String name;
    private String validNum;
    private String totalTicketNum;
    private String locked;
    private String earnings;
    private String deadLine;
    private String transactionTime;
    private String price;
    private String walletAddress;
    private String sequence;
}
