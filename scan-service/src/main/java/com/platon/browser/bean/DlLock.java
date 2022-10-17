package com.platon.browser.bean;

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

    public void setLockBalance(String lockBalance) {
        this.lockBalance = Numeric.decodeQuantity(lockBalance);
    }

    public BigInteger getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(String freeBalance) {
        this.freeBalance = Numeric.decodeQuantity(freeBalance);
    }

    public BigInteger getEpoch() {
        return epoch;
    }

    public void setEpoch(BigInteger epoch) {
        this.epoch = epoch;
    }

}
