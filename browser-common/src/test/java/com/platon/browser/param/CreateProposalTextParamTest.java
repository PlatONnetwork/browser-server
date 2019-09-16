package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CreateProposalTextParamTest {

	@Test
	public void testInit() {
		CreateProposalTextParam param = new CreateProposalTextParam();
		param.init("verifier","pIDID");
		assertTrue("verifier".equals(param.getVerifier()));
	}

}
