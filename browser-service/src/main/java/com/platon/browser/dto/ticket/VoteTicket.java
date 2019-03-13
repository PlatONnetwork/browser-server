package com.platon.browser.dto.ticket;

import lombok.Data;

import java.math.BigInteger;

@Data
public class VoteTicket {
    private Long BlockNumber;
    private String CandidateId;
    private BigInteger Deposit;
    private String Owner;
    private Long RBlockNumber;
    private Long State;
    private String TicketId;
}