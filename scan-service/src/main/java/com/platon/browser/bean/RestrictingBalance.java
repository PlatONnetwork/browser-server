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

    private BigInteger restrictingPlanLockedAmount; //EOA的锁仓未释放余额, 即可用于质押或委托的锁仓金额

    private BigInteger restrictingPlanPledgeAmount; // 已用于质押或委托的锁仓金额

    // 锁定结束的委托金，资金来源是用户账户余额
    private BigInteger delegationUnLockedFreeBalance;

    // 锁定结束的委托金，资金来源是锁仓计划。用户来领取委托金时，一部分可以直接释放到用户账户；一部分可能重新回到锁仓计划中
    private BigInteger delegationUnLockedRestrictingPlanAmount;

    /**
     * 委托锁定中锁定的列表
     */
    private List<DlLock> delegationLockedItems;

    public void setFreeBalance(String freeBalance) {
        this.freeBalance = Numeric.decodeQuantity(freeBalance);
    }

    public void setRestrictingPlanLockedAmount(String restrictingPlanLockedAmount) {
        this.restrictingPlanLockedAmount = Numeric.decodeQuantity(restrictingPlanLockedAmount);
    }

    public void setLockBalance(BigInteger lockBalance) {
        this.restrictingPlanLockedAmount =lockBalance;
    }


    public void setRestrictingPlanPledgeAmount(String restrictingPlanPledgeAmount) {
        this.restrictingPlanPledgeAmount = Numeric.decodeQuantity(restrictingPlanPledgeAmount);
    }

    public void setPledgeBalance(BigInteger pledgeBalance) {
        this.restrictingPlanPledgeAmount = pledgeBalance;
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

    public void setDelegationUnLockedFreeBalance(String delegationUnLockedFreeBalance) {
        this.delegationUnLockedFreeBalance = Numeric.decodeQuantity(delegationUnLockedFreeBalance);
    }

    public BigInteger getDelegationUnLockedRestrictingPlanAmount() {
        return delegationUnLockedRestrictingPlanAmount;
    }

    public void setDelegationUnLockedRestrictingPlanAmount(String delegationUnLockedRestrictingPlanAmount) {
        this.delegationUnLockedRestrictingPlanAmount = Numeric.decodeQuantity(delegationUnLockedRestrictingPlanAmount);
    }

    public List<DlLock> getDelegationLockedItems() {
        return delegationLockedItems;
    }

    public void setDelegationLockedItems(List<DlLock> delegationLockedItems) {
        this.delegationLockedItems = delegationLockedItems;
    }

}
