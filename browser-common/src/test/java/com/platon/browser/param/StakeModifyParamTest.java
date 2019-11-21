package com.platon.browser.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StakeModifyParamTest {

	@Test
	public void testInit() {
		StakeModifyParam param = StakeModifyParam.builder()
                .benefitAddress("sdfdf")
                .blockNumber(BigInteger.ONE)
                .details("sfdsfs")
                .externalId("sdff")
                .nodeId("sdff")
                .nodeName("sfsdf")
                .perNodeName("fsfs")
                .website("were")
                .build();
		assertTrue("sdff".equals(param.getNodeId()));
	}

}
