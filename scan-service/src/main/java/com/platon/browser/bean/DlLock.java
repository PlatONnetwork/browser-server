package com.platon.browser.bean;

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
    private BigInteger restrictingPlanAmount;

    /**
     * 自由金金额
     */
    private BigInteger freeBalance;

    /**
     * 解锁结算周期
     */
    private BigInteger expiredEpoch;

    public BigInteger getRestrictingPlanAmount() {
        return restrictingPlanAmount;
    }

    public void setRestrictingPlanAmount(BigInteger restrictingPlanAmount) {
        this.restrictingPlanAmount = restrictingPlanAmount;
    }

    public BigInteger getFreeBalance() {
        return freeBalance;
    }

    public void setFreeBalance(BigInteger freeBalance) {
        this.freeBalance = freeBalance;
    }

    public BigInteger getExpiredEpoch() {
        return expiredEpoch;
    }

    public void setExpiredEpoch(BigInteger expiredEpoch) {
        this.expiredEpoch = expiredEpoch;
    }

}
