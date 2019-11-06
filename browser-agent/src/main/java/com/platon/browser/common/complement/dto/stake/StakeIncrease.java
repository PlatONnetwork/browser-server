package com.platon.browser.common.complement.dto.stake;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 增持质押 入库参数
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeIncrease extends BusinessParam {
    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 增持金额
     */
    private BigDecimal amount;

    /**
     * 交易快高
     */
    private BigInteger bNum;

    /**
     * 质押交易所在块高
     */
    private BigInteger stakingBlockNum;

    /**
     * 时间
     */
    private Date time;

    /**
     * 交易hash
     */
    private String txHash;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_INCREASE;
    }
}
