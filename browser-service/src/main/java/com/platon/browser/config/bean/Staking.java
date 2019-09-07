
package com.platon.browser.config.bean;

import lombok.Data;

@Data
public class Staking {
    private long StakeThreshold;
    private long MinimumThreshold;
    private int EpochValidatorNum;
    private int HesitateRatio;
    private int UnStakeFreezeRatio;
    private int ActiveUnDelegateFreezeRatio;
}
