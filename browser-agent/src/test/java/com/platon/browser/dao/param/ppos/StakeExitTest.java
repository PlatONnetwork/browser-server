package com.platon.browser.dao.param.ppos;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeExitTest extends TestBase {

    @Test
    public void test(){
        StakeExit target = StakeExit.builder()
                .nodeId("null")
                .stakingBlockNum(BigInteger.ONE)
                .stakingReductionEpoch(4)
                .time(new Date())
                .build();
        target.setNodeId(null)
                .setStakingBlockNum(null)
                .setStakingReductionEpoch(3)
                .setTime(null);

        target.getNodeId();
        target.getStakingBlockNum();
        target.getStakingReductionEpoch();
        target.getTime();
        target.getBusinessType();
        assertTrue(true);
    }
}
