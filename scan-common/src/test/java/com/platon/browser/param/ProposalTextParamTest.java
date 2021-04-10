package com.platon.browser.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
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
