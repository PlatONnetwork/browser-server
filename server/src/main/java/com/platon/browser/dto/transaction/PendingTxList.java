package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class PendingTxList {
    private String txHash;
    private long dwellTime;
    private int energonLimit;
    private int energonPrice;
    private String from;
    private String to;
    private String value;
    private String txType;
    private long serverTime;
}
