package com.platon.browser.bean;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 19:27
 * @Description:
 */
public class RestrictingBalance {

    private String account;

    private BigInteger freeBalance;

    private BigInteger restrictingPlanLockedAmount;

    private BigInteger restrictingPlanPledgeAmount;

    /**
     * 委托锁定待提取中余额部分
     */
    private BigInteger delegationUnLockedFreeBalance;

    /**
     * 委托锁定待提取中锁仓部分
     */
    private BigInteger delegationUnLockedRestrictingPlanAmount;

    /**
     * 委托锁定中锁定的列表
     */
    private List<DlLock> dlLocks;

    public void setFreeBalance(BigInteger freeBalance) {
        this.freeBalance = freeBalance;
    }

    public void setRestrictingPlanLockedAmount(BigInteger restrictingPlanLockedAmount) {
        this.restrictingPlanLockedAmount = restrictingPlanLockedAmount;
    }

    public void setRestrictingPlanPledgeAmount(BigInteger restrictingPlanPledgeAmount) {
        this.restrictingPlanPledgeAmount = restrictingPlanPledgeAmount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigInteger getFreeBalance() {
        return freeBalance;
    }

    public BigInteger getRestrictingPlanLockedAmount() {
        return restrictingPlanLockedAmount;
    }

    public BigInteger getRestrictingPlanPledgeAmount() {
        return restrictingPlanPledgeAmount;
    }

    public BigInteger getDelegationUnLockedFreeBalance() {
        return delegationUnLockedFreeBalance;
    }

    public void setDelegationUnLockedFreeBalance(BigInteger delegationUnLockedFreeBalance) {
        this.delegationUnLockedFreeBalance = delegationUnLockedFreeBalance;
    }

    public BigInteger getDelegationUnLockedRestrictingPlanAmount() {
        return delegationUnLockedRestrictingPlanAmount;
    }

    public void setDelegationUnLockedRestrictingPlanAmount(BigInteger delegationUnLockedRestrictingPlanAmount) {
        this.delegationUnLockedRestrictingPlanAmount = delegationUnLockedRestrictingPlanAmount;
    }

    public List<DlLock> getDlLocks() {
        return dlLocks;
    }

    public void setDlLocks(List<DlLock> dlLocks) {
        this.dlLocks = dlLocks;
    }

}
