
package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class EconomicConfigResult {
    @JSONField(name = "Common")
    private Common common;
    @JSONField(name = "Staking")
    private Staking staking;
    @JSONField(name = "Slashing")
    private Slashing slashing;
    @JSONField(name = "Gov")
    private Gov gov;
    @JSONField(name = "Reward")
    private Reward reward;
}
