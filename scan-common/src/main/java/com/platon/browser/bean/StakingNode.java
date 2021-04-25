package com.platon.browser.bean;

import com.platon.browser.dao.entity.Staking;
import lombok.Data;

import java.math.BigDecimal;

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
    private Integer unStakeFreezeDuration;
}
