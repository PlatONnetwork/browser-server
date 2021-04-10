package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalUpgradeParamTest {

	@Test
	public void testInit() {
		ProposalUpgradeParam param = ProposalUpgradeParam.builder()
                .endVotingRound(BigDecimal.ONE)
                .newVersion(33)
                .nodeName("SFSDF")
                .pIDID("FSDF")
                .verifier("SDF")
                .build();
		assertTrue("SDF".equals(param.getVerifier()));
	}

}
