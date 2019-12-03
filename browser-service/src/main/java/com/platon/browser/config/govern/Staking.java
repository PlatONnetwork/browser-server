package com.platon.browser.config.govern;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 可修改质押配置
 * @author: chendongming@juzix.net
 * @create: 2019-11-25 18:30:11
 **/
@Data
@Builder
public class Staking {
    private BigDecimal stakeThreshold;
    private BigDecimal operatingThreshold;
    private BigDecimal maxValidators;
    private BigDecimal unStakeFreezeDuration;
}
