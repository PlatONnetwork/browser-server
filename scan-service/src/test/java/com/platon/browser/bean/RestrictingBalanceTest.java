package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.TestCase.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RestrictingBalanceTest {
    @Test
    public void test(){
        RestrictingBalance rb = new RestrictingBalance();
        rb.setAccount("sfsdfsfs");
        rb.setFreeBalance("0x99999");
        rb.setRestrictingPlanLockedAmount("0x99999");
        rb.setRestrictingPlanPledgeAmount("0x99999");

        rb.getAccount();
        rb.getFreeBalance();
        rb.getRestrictingPlanLockedAmount();
        rb.getRestrictingPlanPledgeAmount();

        assertTrue(true);
    }
}
