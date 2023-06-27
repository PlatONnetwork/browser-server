package com.platon.browser.bean;

import com.platon.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 19:27
 * @Description:
 */
public class RestrictingBalance {

    private String account;

    private BigInteger freeBalance; //EOA帐号余额

    private BigInteger lockBalance; //EOA的锁仓未释放余额

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

    public void setFreeBalance(String freeBalance) {
        this.freeBalance = Numeric.decodeQuantity(freeBalance);
    }

    public void setLockBalance(String lockBalance) {
        this.lockBalance = Numeric.decodeQuantity(lockBalance);
    }

    public void setPledgeBalance(String pledgeBalance) {
        this.pledgeBalance = Numeric.decodeQuantity(pledgeBalance);
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

    public void setDlFreeBalance(String dlFreeBalance) {
        this.dlFreeBalance = Numeric.decodeQuantity(dlFreeBalance);
    }

    public BigInteger getDlRestrictingBalance() {
        return dlRestrictingBalance;
    }

    public void setDlRestrictingBalance(String dlRestrictingBalance) {
        this.dlRestrictingBalance = Numeric.decodeQuantity(dlRestrictingBalance);
    }

    public List<DlLock> getDlLocks() {
        return dlLocks;
    }

    public void setDlLocks(List<DlLock> dlLocks) {
        this.dlLocks = dlLocks;
    }

}
