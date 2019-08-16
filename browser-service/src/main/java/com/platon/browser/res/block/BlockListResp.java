package com.platon.browser.res.block;

import lombok.Data;

@Data
public class BlockListResp {
    private Long number;
    private Long timestamp;
    private Long serverTime;
    private Integer statTxQty;
    private Integer size;
    private String nodeName;
    private String nodeId;
    private String gasUsed;
    private String statTxGasLimit;
    private String blockReward;
}
