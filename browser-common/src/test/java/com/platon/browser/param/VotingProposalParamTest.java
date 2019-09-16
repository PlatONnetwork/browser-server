package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VotingProposalParamTest {

	@Test
	public void testInit() {
		VotingProposalParam param = new VotingProposalParam();
		param.init("verifier","proposalId","option","0.7.0","versionSign");
		assertTrue("verifier".equals(param.getVerifier()));
	}

}
