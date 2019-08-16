package com.platon.browser.res.block;

import lombok.Data;

@Data
public class BlockDetailResp {
    private Long number;
    private Long timestamp;
    private Integer statTxQty;
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
    private Integer statTransferQty;
    private Integer statDelegateQty;
    private Integer statStakingQty;
    private Integer statProposalQty;
    
}
