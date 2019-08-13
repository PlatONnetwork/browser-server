package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.proposal.ProposalDetailsResp;
import com.platon.browser.res.proposal.ProposalListResp;
import com.platon.browser.res.proposal.VoteListResp;

@RestController
public class AppDocProposalController implements AppDocProposal {

	@Override
	public BaseResp<ProposalListResp> proposalList(@Valid PageReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<ProposalDetailsResp> proposalDetails(@Valid ProposalDetailRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<VoteListResp> voteList(@Valid VoteListRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

}
