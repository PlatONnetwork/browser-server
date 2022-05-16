package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StakingBO {

    /**
     * 总质押
     */
    private BigDecimal totalStakingValue;

    /**
     * 质押率分母
     */
    private BigDecimal stakingDenominator;

}
