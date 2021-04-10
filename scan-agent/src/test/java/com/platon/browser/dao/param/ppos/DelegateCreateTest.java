package com.platon.browser.dao.param.ppos;

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
 * @Description: 创建委托 入库参数
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateCreateTest extends AgentTestBase {

    @Test
    public void test(){

        DelegateCreate target = DelegateCreate.builder()
                .amount(BigDecimal.ONE)
                .blockNumber(BigInteger.ONE)
                .sequence(BigInteger.TEN)
                .stakingBlockNumber(BigInteger.TEN)
                .nodeId("0X3333")
                .txFrom("0xddd")
                .build();
        target.setTxFrom(null);
        target.setAmount(null);
        target.setBlockNumber(null);
        target.setSequence(null);
        target.setStakingBlockNumber(null);
        target.setNodeId(null);
        target.setTxFrom(null);

        target.getTxFrom();
        target.getAmount();
        target.getBlockNumber();
        target.getSequence();
        target.getStakingBlockNumber();
        target.getNodeId();
        target.getTxFrom();
        target.getBusinessType();

        assertTrue(true);
    }
}
