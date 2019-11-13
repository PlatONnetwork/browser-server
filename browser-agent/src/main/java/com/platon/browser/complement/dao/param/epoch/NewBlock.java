package com.platon.browser.complement.dao.param.epoch;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 新区块更新入参
 */
@Data
@Builder
@Accessors(chain = true)
public class NewBlock implements BusinessParam {
    /**
     * 出块奖励（交易手续费）
     */
    private BigDecimal feeRewardValue;

    /**
     * 区块奖励（激励池）
     */
    private BigDecimal blockRewardValue;

    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 质押所在区块号
     */
    private BigInteger stakingBlockNum;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.NEW_BLOCK;
    }
}
