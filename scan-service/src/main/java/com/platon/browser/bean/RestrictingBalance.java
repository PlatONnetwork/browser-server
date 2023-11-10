package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 19:27
 * @Description:
 */
@Data
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
}
