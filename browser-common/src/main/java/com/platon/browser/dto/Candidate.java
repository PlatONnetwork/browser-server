package com.platon.browser.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Candidate {
    private BigInteger Deposit;
    private Long BlockNumber;
    private Long TxIndex;
    private String CandidateId;
    private String Host;
    private String Port;
    private String Owner;
    private String From;
    private String Extra;
    private BigInteger Fee;
    private String TicketId;
}
