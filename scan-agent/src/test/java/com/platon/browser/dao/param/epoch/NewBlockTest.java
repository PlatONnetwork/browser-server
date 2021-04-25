package com.platon.browser.dao.param.epoch;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 新区块更新入参
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class NewBlockTest extends AgentTestBase {

    @Test
    public void test(){
        NewBlock target = NewBlock.builder()
                .blockRewardValue(BigDecimal.TEN)
                .feeRewardValue(BigDecimal.TEN)
                .nodeId("0xdd")
                .stakingBlockNum(BigInteger.TEN)
                .build();
        target.setBlockRewardValue(null)
                .setFeeRewardValue(null)
                .setNodeId(null)
                .setStakingBlockNum(null);
        target.getBlockRewardValue();
        target.getFeeRewardValue();
        target.getNodeId();
        target.getStakingBlockNum();
        target.getBusinessType();

        assertTrue(true);
    }
}
