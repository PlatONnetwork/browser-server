package com.platon.browser.bean;

import com.platon.utils.Numeric;

import java.math.BigInteger;

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

    public void setFreeBalance(String freeBalance) {
        this.freeBalance = Numeric.decodeQuantity(freeBalance);
    }

    public void setRestrictingPlanLockedAmount(String restrictingPlanLockedAmount) {
        this.restrictingPlanLockedAmount = Numeric.decodeQuantity(restrictingPlanLockedAmount);
    }

    public void setRestrictingPlanPledgeAmount(String restrictingPlanPledgeAmount) {
        this.restrictingPlanPledgeAmount = Numeric.decodeQuantity(restrictingPlanPledgeAmount);
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

}
