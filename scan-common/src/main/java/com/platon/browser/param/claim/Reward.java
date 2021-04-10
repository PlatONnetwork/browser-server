package com.platon.browser.param.claim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class Reward {
    private String nodeId;
    private String nodeName;
    private BigInteger stakingNum;
    private BigDecimal reward;
}
