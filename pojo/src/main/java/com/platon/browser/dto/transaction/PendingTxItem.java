package com.platon.browser.dto.transaction;

import lombok.Data;

@Data
public class PendingTxItem {
    private String txHash;
    private long dwellTime;
    private String energonLimit;
    private String energonPrice;
    private String from;
    private String to;
    private String value;
    private String txType;
    private long serverTime;
}
