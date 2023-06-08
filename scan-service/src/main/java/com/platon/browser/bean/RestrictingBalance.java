package com.platon.browser.bean;

import com.alibaba.fastjson.annotation.JSONField;

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

    private BigInteger lockBalance;

    private BigInteger pledgeBalance;

    /**
     * 委托锁定待提取中余额部分
     */
    private BigInteger dlFreeBalance;

    /**
     * 委托锁定待提取中锁仓部分
     */
    private BigInteger dlRestrictingBalance;

    /**
     * 委托锁定中锁定的列表
     */
    private List<DlLock> dlLocks;

    public void setFreeBalance(BigInteger freeBalance) {
        this.freeBalance = freeBalance;
    }

    @JSONField(name = "restrictingPlanLockedAmount")
    public void setLockBalance(BigInteger lockBalance) {
        this.lockBalance = lockBalance;
    }

    @JSONField(name = "restrictingPlanPledgeAmount")
    public void setPledgeBalance(BigInteger pledgeBalance) {
        this.pledgeBalance = pledgeBalance;
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

    public BigInteger getLockBalance() {
        return lockBalance;
    }

    public BigInteger getPledgeBalance() {
        return pledgeBalance;
    }

    public BigInteger getDlFreeBalance() {
        return dlFreeBalance;
    }

    @JSONField(name = "delegationUnLockedFreeBalance")
    public void setDlFreeBalance(BigInteger dlFreeBalance) {
        this.dlFreeBalance = dlFreeBalance;
    }

    public BigInteger getDlRestrictingBalance() {
        return dlRestrictingBalance;
    }

    @JSONField(name = "delegationUnLockedRestrictingPlanAmount")
    public void setDlRestrictingBalance(BigInteger dlRestrictingBalance) {
        this.dlRestrictingBalance = dlRestrictingBalance;
    }

    public List<DlLock> getDlLocks() {
        return dlLocks;
    }

    public void setDlLocks(List<DlLock> dlLocks) {
        this.dlLocks = dlLocks;
    }

}
