//package com.platon.browser.service.impl;
//
//
//import com.platon.browser.TestBase;
//import com.platon.browser.service.ProposalService;
//import com.platon.browser.service.VoteService;
//import com.platon.browser.req.PageReq;
//import com.platon.browser.req.proposal.ProposalDetailRequest;
//import com.platon.browser.req.proposal.VoteListRequest;
//import com.platon.browser.res.RespPage;
//import com.platon.browser.res.proposal.ProposalListResp;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//
//import org.junit.Before;
//
//public class ProposalServiceImplTest extends TestBase{
//    @Autowired
//    private ProposalService proposalService;
//    @Autowired
//    private VoteService voteService;
//    
//    private String proHash = "";
//
//    @Before
//	public void init() {
//    	PageReq pageReq = new PageReq();
//    	RespPage<ProposalListResp> pages = proposalService.list(pageReq);
//    	if(pages.getData().size()>0) {
//    		proHash = pages.getData().get(0).getProposalHash();
//    	}
//    }
//    
//    @Test
//    public void list() {
//        PageReq pageReq = new PageReq();
//        RespPage<ProposalListResp> pages = proposalService.list(pageReq);
//        assertTrue(pages.getData().size()>=0);
//    }
//
//    @Test
//    public void get() {
//        ProposalDetailRequest request = new ProposalDetailRequest();
//        request.setProposalHash("0x529cddffab0b0a3b4c2c6df10a9fcbaa452d3ac20e987f9e5a1b11f5b15c3972");
//        assertNotNull(proposalService.get(request));
//        request.setProposalHash(proHash);
//        assertNotNull(proposalService.get(request));
//    }
//    @Test
//    public void queryByProposal() {
//        VoteListRequest request = new VoteListRequest();
//        request.setProposalHash("0x529cddffab0b0a3b4c2c6df10a9fcbaa452d3ac20e987f9e5a1b11f5b15c3972");
//        assertNotNull(voteService.queryByProposal(request));
//    }
//}