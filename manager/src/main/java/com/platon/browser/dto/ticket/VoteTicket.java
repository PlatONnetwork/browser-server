package com.platon.browser.dto.ticket;

import lombok.Data;

@Data
public class VoteTicket {
    private Long BlockNumber;
    private String CandidateId;
    private Long Deposit;
    private String Owner;
    private Long RBlockNumber;
    private Long State;
    private String TicketId;
}