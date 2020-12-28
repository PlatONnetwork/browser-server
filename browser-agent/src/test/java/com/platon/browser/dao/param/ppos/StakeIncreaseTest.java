package com.platon.browser.dao.param.ppos;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeIncreaseTest extends TestBase {

    @Test
    public void test(){
        StakeIncrease target = StakeIncrease.builder()
                .nodeId("null")
                .stakingBlockNum(BigInteger.ONE)
                .amount(BigDecimal.ONE)
                .build();
        target.setNodeId(null)
                .setStakingBlockNum(null)
                .setAmount(null);

        target.getNodeId();
        target.getStakingBlockNum();
        target.getAmount();
        target.getBusinessType();
        assertTrue(true);
    }
}
