package com.platon.browser.param;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ProposalTextParamTest {

	@Test
	public void testInit() {
		ProposalTextParam param = ProposalTextParam.builder()
                .nodeName("0xfsfsf")
                .pIDID("dsfsfsdf")
                .verifier("fsffdf")
                .build();
		assertTrue("fsffdf".equals(param.getVerifier()));
	}

}
