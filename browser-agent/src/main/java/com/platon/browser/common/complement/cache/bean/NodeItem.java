package com.platon.browser.common.complement.cache.bean;

import com.platon.browser.common.complement.bean.AnnualizedRateInfo;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Builder
@Accessors(chain = true)
public class NodeItem {
    private String nodeId;
    private String nodeName;
    private Integer stakingTxIndex;
    private AnnualizedRateInfo annualizedRateInfo;
    // 最新的质押区块号,随验证人创建交易更新
    private BigInteger stakingBlockNum;
}
