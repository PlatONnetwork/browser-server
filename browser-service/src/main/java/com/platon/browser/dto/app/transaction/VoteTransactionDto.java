package com.platon.browser.dto.app.transaction;

import lombok.Data;

import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/16 16:58
 * @Description:
 */
@Data
public class VoteTransactionDto {
    private String nodeId;
    private String name;
    private int validNum;
    private int totalTicketNum;
    private long locked;
    private long earnings;
    private long deadLine;
    private long transactionTime;
    private String deposit;
    private String owner;
}
