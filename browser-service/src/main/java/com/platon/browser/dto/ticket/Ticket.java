package com.platon.browser.dto.ticket;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Ticket {
    private String ticketId;
    private String txHash;
    private String candidateId;
    private String owner;
    private Long blockNumber;
    private Long rblockNumber;
    private Integer state;
    private BigDecimal income;
    private Date estimateExpireTime;
    private Date actualExpireTime;
}