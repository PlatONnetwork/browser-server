package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalVoteParamTest {

	@Test
	public void testInit() {
		ProposalVoteParam param = ProposalVoteParam.builder()
                .nodeName("fsfsf")
                .option("dsfdfsf")
                .pIDID("fsdfs")
                .programVersion("sfsdf")
                .proposalId("fsfs")
                .proposalType("fsdf")
                .url("sfsf")
                .verifier("fsfs")
                .versionSign("sdfsf")
                .build();
		assertTrue("fsfs".equals(param.getVerifier()));
	}

}
