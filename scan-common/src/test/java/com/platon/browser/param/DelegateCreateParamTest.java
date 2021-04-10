package com.platon.browser.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DelegateCreateParamTest {

	private DelegateCreateParam param;

	@Test
	public void testInit() {
        param = DelegateCreateParam.builder()
                .amount(BigDecimal.ONE)
                .nodeId("sdfsf")
                .nodeName("sdfsf")
                .stakingBlockNum(BigInteger.ONE)
                .type(3)
                .build();
		assertTrue(param.getNodeId().equals("sdfsf"));
	}

}
