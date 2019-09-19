package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Staking {
    // 验证人最低的质押Token数(VON)
    @JSONField(name = "StakeThreshold")
    private BigDecimal stakeThreshold;
    // 委托人每次委托及赎回的最低Token数(VON)
    @JSONField(name = "MinimumThreshold")
    private BigDecimal minimumThreshold;
    // 每个结算周期内验证人数（24或101）
    @JSONField(name = "EpochValidatorNum")
    private BigDecimal epochValidatorNum;
    // 犹豫期是几个结算周期v
    @JSONField(name = "HesitateRatio")
    private BigDecimal hesitateRatio;
    // 节点质押退回锁定周期
    @JSONField(name = "UnStakeFreezeRatio")
    private BigInteger unStakeFreezeRatio;
    // 解除委托锁定的结算周期
    @JSONField(name = "ActiveUnDelegateFreezeRatio")
    private BigDecimal activeUnDelegateFreezeRatio;
}
