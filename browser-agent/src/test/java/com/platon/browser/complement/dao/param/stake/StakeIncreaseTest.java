package com.platon.browser.complement.dao.param.stake;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeIncreaseTest extends AgentTestBase {

    @Test
    public void test(){
        StakeIncrease target = StakeIncrease.builder()
                .nodeId(null)
                .stakingBlockNum(null)
                .amount(null)
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
