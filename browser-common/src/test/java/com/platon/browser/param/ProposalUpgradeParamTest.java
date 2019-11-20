package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

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
