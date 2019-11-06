package com.platon.browser.common.complement.dto.epoch;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 结算周期切换参数入库
 */
@Data
@Builder
@Slf4j
@Accessors(chain = true)
public class Settle  extends BusinessParam {

    /**
     * 当前结算周期验证人
     */
    private List<String> curVerifierList;

    /**
     * 上轮结算周期验证人
     */
    private List<String> preVerifierList;

    /**
     * 期望年化率
     */
    private Double annualizedRate;

    /**
     *  结算周期
     */
    private int settingEpoch;

    /**
     * 解除质押锁定金额的轮数
     */
    private int stakingLockEpoch;

    /**
     * 期望年化率依赖的数据
     */
    private String annualizedRateInfo;

    /**
     * 出块奖励统计(手续费)(von)
     */
    private BigDecimal feeRewardValue;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.SETTLE_EPOCH;
    }
}