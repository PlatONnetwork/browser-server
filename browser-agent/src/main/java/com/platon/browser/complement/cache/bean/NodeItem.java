package com.platon.browser.complement.cache.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class NodeItem {
    private String nodeId;
    private String nodeName;
    private Integer stakingTxIndex;
    private AnnualizedRateInfo annualizedRateInfo;
}
