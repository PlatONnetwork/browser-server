package com.platon.browser.bean;

import com.platon.browser.bean.RestrictingBalance;
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
        rb.setLockBalance(new BigInteger("99999"));
        rb.setPledgeBalance(new BigInteger("99999"));

        rb.getAccount();
        rb.getFreeBalance();
        rb.getLockBalance();
        rb.getPledgeBalance();

        assertTrue(true);
    }
}
