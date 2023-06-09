package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RestrictingBalanceTest {
    @Test
    public void test(){
        RestrictingBalance rb = new RestrictingBalance();
        rb.setAccount("sfsdfsfs");
        rb.setFreeBalance(new BigInteger("99999"));
        rb.setRestrictingPlanLockedAmount(new BigInteger("99999"));
        rb.setRestrictingPlanPledgeAmount(new BigInteger("99999"));

        rb.getAccount();
        rb.getFreeBalance();
        rb.getRestrictingPlanLockedAmount();
        rb.getRestrictingPlanPledgeAmount();

        assertTrue(true);
    }
}
