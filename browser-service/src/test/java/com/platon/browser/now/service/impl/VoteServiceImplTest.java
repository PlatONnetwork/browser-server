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
		request.setProposalHash("0xb851e45c6894ecd2737aad0112001d64fd8e14877010767406665f5efe9f45e7");
		RespPage<VoteListResp> resp = voteService.queryByProposal(request);
		assertNotNull(resp);
	}

}
