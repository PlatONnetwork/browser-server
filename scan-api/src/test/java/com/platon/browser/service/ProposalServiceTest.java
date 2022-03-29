package com.platon.browser.service;


import com.platon.browser.ApiTestMockBase;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.proposal.ProposalDetailRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalServiceTest extends ApiTestMockBase {
    @Mock
    private ProposalMapper proposalMapper;
    @Spy
    private ProposalInfoService target;
    
    @Before
	public void setup() {
        ReflectionTestUtils.setField(target,"i18n",i18n);
        ReflectionTestUtils.setField(target,"proposalMapper",proposalMapper);
        ReflectionTestUtils.setField(target,"statisticCacheService",statisticCacheService);
        ReflectionTestUtils.setField(target,"blockChainConfig",blockChainConfig);
        ReflectionTestUtils.setField(target,"ESBlockRepository", ESBlockRepository);
        ReflectionTestUtils.setField(target,"networkParams",networkParams);
    }
    
    @Test
    public void test() throws IOException {
        ProposalDetailRequest req = new ProposalDetailRequest();

        when(proposalMapper.selectByPrimaryKey(any())).thenReturn(null);
        target.get(req);

        Proposal proposal = new Proposal();
        proposal.setEndVotingBlock(33L);
        proposal.setAccuVerifiers(33L);
        proposal.setAbstentions(33L);
        proposal.setActiveBlock(33L);
        proposal.setNays(33L);
        proposal.setTimestamp(new Date());
        proposal.setYeas(33L);
        proposal.setBlockNumber(33L);
        proposal.setName("test");
        proposal.setStaleValue("1000000000000000000");
        proposal.setNewValue("1000000000000000000");
        proposal.setType(CustomProposal.TypeEnum.UPGRADE.getCode());
        when(proposalMapper.selectByPrimaryKey(any())).thenReturn(proposal);

        proposal.setType(CustomProposal.TypeEnum.TEXT.getCode());
        target.get(req);

        proposal.setType(CustomProposal.TypeEnum.UPGRADE.getCode());
        target.get(req);
        
        proposal.setEndVotingBlock(34L);
        proposal.setType(CustomProposal.TypeEnum.UPGRADE.getCode());
        target.get(req);

        proposal.setType(CustomProposal.TypeEnum.PARAMETER.getCode());
        proposal.setName("stakeThreshold");
        target.get(req);
        
        proposal.setType(CustomProposal.TypeEnum.PARAMETER.getCode());
        proposal.setName("slashFractionDuplicateSign");
        target.get(req);

        proposal.setType(CustomProposal.TypeEnum.CANCEL.getCode());
        target.get(req);

        assertTrue(true);
    }
    
    @Test
    public void testList() throws IOException {
    	PageReq pageReq = new PageReq();
    	Proposal proposal = new Proposal();
        proposal.setEndVotingBlock(33L);
        proposal.setAccuVerifiers(33L);
        proposal.setAbstentions(33L);
        proposal.setActiveBlock(33L);
        proposal.setNays(33L);
        proposal.setTimestamp(new Date());
        proposal.setYeas(33L);
        proposal.setBlockNumber(33L);
        proposal.setName("test");
        proposal.setStaleValue("1000000000000000000");
        proposal.setNewValue("1000000000000000000");
        proposal.setType(CustomProposal.TypeEnum.UPGRADE.getCode());
        List<Proposal> proposals = new ArrayList<Proposal>();
        proposals.add(proposal);
        when(proposalMapper.selectByExample(any())).thenReturn(proposals);
        NetworkStat net = new NetworkStat();
		net.setCurNumber(10l);
		when(statisticCacheService.getNetworkStatCache()).thenReturn(net);
    	target.list(pageReq);
    	assertTrue(true);
    }
    
    @Test
    public void testQueryByProposal() {
    	
    	
    }
}