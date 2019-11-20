package com.platon.browser.complement.dao.param.delegate;

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
                .amount(BigDecimal.ONE)
                .blockNumber(BigInteger.ONE)
                .codeDelegateHes(BigDecimal.ZERO)
                .codeDelegateLocked(BigDecimal.TEN)
                .codeDelegateReleased(BigDecimal.TEN)
                .codeRmDelegateHes(BigDecimal.TEN)
                .codeRmDelegateLocked(BigDecimal.TEN)
                .codeRmDelegateReleased(BigDecimal.TEN)
                .codeIsHistory(1)
                .codeNodeIsLeave(false)
                .codeRealAmount(BigDecimal.TEN)
                .codeNodeIsLeave(true)
                .minimumThreshold(BigDecimal.TEN)
                .nodeId("0xdfd")
                .stakingBlockNumber(BigInteger.ONE)
                .txFrom("0x33")
                .build();
        target.setAmount(null);
        target.setBlockNumber(null);
        target.setCodeDelegateHes(null);
        target.setCodeDelegateLocked(null);
        target.setCodeDelegateReleased(null);
        target.setCodeRmDelegateHes(null);
        target.setCodeRmDelegateLocked(null);
        target.setCodeRmDelegateReleased(null);
        target.setCodeIsHistory(1);
        target.setCodeNodeIsLeave(false);
        target.setCodeRealAmount(null);
        target.setCodeNodeIsLeave(true);
        target.setMinimumThreshold(null);
        target.setNodeId(null);
        target.setStakingBlockNumber(null);
        target.setTxFrom(null);

        target.getAmount();
        target.getBlockNumber();
        target.getCodeDelegateHes();
        target.getCodeDelegateLocked();
        target.getCodeDelegateReleased();
        target.getCodeRmDelegateHes();
        target.getCodeRmDelegateLocked();
        target.getCodeRmDelegateReleased();
        target.getCodeIsHistory();
        target.isCodeNodeIsLeave();
        target.getCodeRealAmount();
        target.isCodeNodeIsLeave();
        target.getMinimumThreshold();
        target.getNodeId();
        target.getStakingBlockNumber();
        target.getTxFrom();
        target.getBusinessType();
        assertTrue(true);
    }
}
