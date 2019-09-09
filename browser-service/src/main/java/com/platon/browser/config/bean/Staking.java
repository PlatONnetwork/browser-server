
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Staking {
    private BigDecimal StakeThreshold;
    private BigDecimal MinimumThreshold;
    private BigInteger EpochValidatorNum;
    private BigInteger HesitateRatio;
    private BigInteger UnStakeFreezeRatio;
    private BigInteger ActiveUnDelegateFreezeRatio;
}
