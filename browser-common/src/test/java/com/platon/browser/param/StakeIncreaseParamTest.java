package com.platon.browser.param;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeIncreaseParamTest {

	private StakeIncreaseParam param;

	@Before
	public void setUp() {
		param = StakeIncreaseParam.builder()
                .amount(BigDecimal.ONE)
                .nodeId("sdfsf")
                .nodeName("sdff")
                .stakingBlockNum(BigInteger.ONE)
                .type(3)
                .build();
	}

	@Test
	public void testInit() {
		assertTrue(param.getNodeId().equals("sdfsf"));
	}
}
