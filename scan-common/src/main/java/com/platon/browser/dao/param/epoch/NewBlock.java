package com.platon.browser.dao.param.epoch;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 新区块更新入参
 */
@Data
@Builder
@Accessors(chain = true)
public class NewBlock implements BusinessParam {
    //出块奖励（交易手续费）
    private BigDecimal feeRewardValue;
    //区块奖励（激励池）
    private BigDecimal blockRewardValue;
    //本结算周期预计可获得的质押奖励
    private BigDecimal predictStakingReward;
    //节点Id
    private String nodeId;
    //质押所在区块号
    private BigInteger stakingBlockNum;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.NEW_BLOCK;
    }
}
