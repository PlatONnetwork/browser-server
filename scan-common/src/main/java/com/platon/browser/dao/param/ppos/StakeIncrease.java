package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 增持质押 入库参数
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeIncrease implements BusinessParam {
    //节点Id
    private String nodeId;
    //增持金额
    private BigDecimal amount;
    //质押交易所在块高
    private BigInteger stakingBlockNum;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_INCREASE;
    }
}
