
package com.platon.browser.config.bean;

import lombok.Data;

@Data
public class EconomicConfigResult {
    private Common Common;
    private Staking Staking;
    private Slashing Slashing;
    private Gov Gov;
    private Reward Reward;
}
