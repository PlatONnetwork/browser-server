package com.platon.browser.common.complement.dto.epoch;

import java.math.BigDecimal;
import java.util.List;

import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.enums.BusinessType;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 结算周期切换参数入库
 */
@Data
@Builder
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
     * 质押奖励(von)
     */
    private BigDecimal stakingReward;

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

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.SETTLE_EPOCH;
    }
}