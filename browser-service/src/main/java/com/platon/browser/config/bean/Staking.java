package com.platon.browser.config.bean;

import lombok.Data;
import org.bouncycastle.jcajce.provider.symmetric.ChaCha;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Staking {
    // 验证人最低的质押Token数(VON)
    private BigDecimal StakeThreshold;
    // 委托人每次委托及赎回的最低Token数(VON)
    private BigDecimal MinimumThreshold;
    // 每个结算周期内验证人数（24或101）
    private BigDecimal EpochValidatorNum;
    // 犹豫期是几个结算周期v
    private BigDecimal HesitateRatio;
    // 节点质押退回锁定周期
    private BigInteger UnStakeFreezeRatio;
    // 解除委托锁定的结算周期
    private BigDecimal ActiveUnDelegateFreezeRatio;
}
