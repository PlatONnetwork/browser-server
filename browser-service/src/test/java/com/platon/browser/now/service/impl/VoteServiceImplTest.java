package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.VoteListResp;

public class VoteServiceImplTest extends TestBase{

	@Autowired
	private VoteService voteService;

	@Test
	public void queryByProposal() {
		VoteListRequest request = new VoteListRequest();
		request.setProposalHash("0x529cddffab0b0a3b4c2c6df10a9fcbaa452d3ac20e987f9e5a1b11f5b15c3972");
		RespPage<VoteListResp> resp = voteService.queryByProposal(request);
		assertNotNull(resp);
	}

}
