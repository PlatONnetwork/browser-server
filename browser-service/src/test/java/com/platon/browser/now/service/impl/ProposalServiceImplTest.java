package com.platon.browser.now.service.impl;


import com.platon.browser.TestBase;
import com.platon.browser.now.service.ProposalService;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.ProposalListResp;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ProposalServiceImplTest extends TestBase{
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private VoteService voteService;

    @Test
    public void list() {
        PageReq pageReq = new PageReq();
        RespPage<ProposalListResp> pages = proposalService.list(pageReq);
        assertTrue(pages.getData().size()>=0);
    }

    @Test
    public void get() {
        ProposalDetailRequest request = new ProposalDetailRequest();
        request.setProposalHash("addvdfbnghm");
        assertNotNull(proposalService.get(request));
    }
    @Test
    public void queryByProposal() {
        VoteListRequest request = new VoteListRequest();
        request.setProposalHash("addvdfbnghm");
        assertNotNull(voteService.queryByProposal(request));
    }
}