package com.platon.browser.dao.param.ppos;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Builder
@Accessors(chain = true)
public class ExceptionNodeParam {
    //节点Id
    private String nodeId;
    //质押交易所在块高
    private BigInteger stakingBlockNum;
}
