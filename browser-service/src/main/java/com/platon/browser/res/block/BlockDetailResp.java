package com.platon.browser.res.block;

import lombok.Data;

@Data
public class BlockDetailResp {
    private Long number;
    private Long timestamp;
    private Integer txQty;
    private String hash;
    private String parentHash;
    private String nodeName;
    private String nodeId;
    private Long timeDiff;
    private String gasLimit;
    private String gasUsed;
    private String statTxGasLimit;
    private String blockReward;
    private String extraData;
 // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
    private Integer transferQty;
    private Integer delegateQty;
    private Integer stakingQty;
    private Integer proposalQty;
    private Long serverTime;
    private Integer size;
}
