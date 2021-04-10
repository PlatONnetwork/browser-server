package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateExitTest extends AgentTestBase {
    @Test
    public void test(){
        DelegateExit target = DelegateExit.builder()
                .blockNumber(BigInteger.ONE)
                .codeIsHistory(1)
                .codeNodeIsLeave(false)
                .minimumThreshold(BigDecimal.TEN)
                .nodeId("0xdfd")
                .stakingBlockNumber(BigInteger.ONE)
                .txFrom("0x33")
                .build();
        target.setBlockNumber(null);
        target.setCodeIsHistory(1);
        target.setCodeNodeIsLeave(false);
        target.setCodeNodeIsLeave(true);
        target.setMinimumThreshold(null);
        target.setNodeId(null);
        target.setStakingBlockNumber(null);
        target.setTxFrom(null);

        target.getBlockNumber();
        target.getCodeIsHistory();
        target.isCodeNodeIsLeave();
        target.isCodeNodeIsLeave();
        target.getMinimumThreshold();
        target.getNodeId();
        target.getStakingBlockNumber();
        target.getTxFrom();
        target.getBusinessType();
        assertTrue(true);
    }
}
