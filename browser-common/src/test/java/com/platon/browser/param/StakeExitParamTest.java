package com.platon.browser.param;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class StakeExitParamTest {

	@Test
	public void testInit() {
		StakeExitParam param = StakeExitParam.builder()
                .amount(BigDecimal.ONE)
                .nodeId("DFSF")
                .nodeName("SDFSF")
                .stakingBlockNum(BigInteger.ONE)
                .build();
		assertTrue("DFSF".equals(param.getNodeId()));
	}

}
