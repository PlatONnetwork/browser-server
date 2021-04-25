package com.platon.browser.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeCreateParamTest {

	private StakeCreateParam param;

	@Test
	public void test() {
		param = StakeCreateParam.builder()
        .amount(BigDecimal.ONE)
                .benefitAddress("dfsf")
                .blockNumber(BigInteger.ONE)
                .details("sdfsfs")
                .externalId("sfdfsf")
                .nodeId("sdffs")
                .nodeName("sdfsdf")
                .programVersion(BigInteger.TEN)
                .type(3)
                .website("WERER")
        .build();
		assertTrue("WERER".equals(param.getWebsite()));
	}
}
