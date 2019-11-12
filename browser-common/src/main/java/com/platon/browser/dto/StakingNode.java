package com.platon.browser.dto;

import java.math.BigDecimal;

import com.platon.browser.dao.entity.Staking;

import lombok.Data;

@Data
public class StakingNode extends Staking{

    private Integer statSlashMultiQty;

    private Integer statSlashLowQty;

    private Long statBlockQty;

    private Long statExpectBlockQty;

    private Integer statVerifierTime;

    private Integer isRecommend;

    private BigDecimal totalValue;

    private BigDecimal statDelegateValue;

    private BigDecimal statDelegateReleased;

    private Integer statValidAddrs;

    private Integer statInvalidAddrs;

    private BigDecimal statBlockRewardValue;

    private BigDecimal statFeeRewardValue;

    private BigDecimal statStakingRewardValue;
    
    private Integer nodeStatVerifierTime;
}
