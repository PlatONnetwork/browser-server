package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.platon.utils.Numeric;

import java.math.BigInteger;

/**
 * 委托锁定中锁定的列表
 *
 * @date: 2022/8/26
 */
public class DlLock {

    /**
     * 锁仓金额
     */
    private BigInteger lockBalance;

    /**
     * 自由金金额
     */
    private BigInteger freeBalance;

    /**
     * 解锁结算周期
     */
    private BigInteger epoch;

    public BigInteger getLockBalance() {
        return lockBalance;
    }

    @JSONField(name = "restrictingPlanAmount")
    public void setLockBalance(BigInteger lockBalance) {
        this.lockBalance = lockBalance;
    }

    public BigInteger getFreeBalance() {
        return freeBalance;
    }

    @JSONField(name = "FreeBalance")
    public void setFreeBalance(BigInteger freeBalance) {
        this.freeBalance = freeBalance;
    }

    public BigInteger getEpoch() {
        return epoch;
    }

    @JSONField(name = "expiredEpoch")
    public void setEpoch(BigInteger epoch) {
        this.epoch = epoch;
    }

}
