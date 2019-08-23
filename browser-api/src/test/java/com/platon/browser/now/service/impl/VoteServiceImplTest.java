package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.dto.RespPage;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.proposal.VoteListResp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class VoteServiceImplTest {

	@Autowired
	private VoteService voteService;

	@Test
	public void queryByProposal() {
		VoteListRequest request = new VoteListRequest();
		request.setProposalHash("addvdfbnghm");
		RespPage<VoteListResp> resp = voteService.queryByProposal(request);
		assertNotNull(resp);
	}

}
