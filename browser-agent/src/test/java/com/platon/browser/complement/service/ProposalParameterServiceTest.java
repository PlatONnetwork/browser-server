package com.platon.browser.complement.service;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.epoch.OnConsensusConverter;
import com.platon.browser.complement.converter.epoch.OnElectionConverter;
import com.platon.browser.complement.converter.epoch.OnNewBlockConverter;
import com.platon.browser.complement.converter.epoch.OnSettleConverter;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.mapper.VoteMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务测试
 * @author: chendongming@juzix.net
 * @create: 2019-11-13 11:41:00
 **/
@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalParameterServiceTest extends AgentTestBase {
    @Mock
    private ProposalMapper proposalMapper;
    @Mock
	private VoteMapper voteMapper;
    @Mock
	private ProposalBusinessMapper proposalBusinessMapper;

    @Spy
    private ProposalParameterService target;

    @Before
    public void setup() throws Exception {
        ReflectionTestUtils.setField(target, "proposalMapper", proposalMapper);
        ReflectionTestUtils.setField(target, "voteMapper", voteMapper);
        ReflectionTestUtils.setField(target, "proposalBusinessMapper", proposalBusinessMapper);
    }

    @Test
    public void test() throws Exception {
    	
    	when(voteMapper.selectByExample(any())).thenReturn(Collections.emptyList());
    	target.setSlashParameters("0x1");
    	
    	List<Vote> votes = new ArrayList<Vote>();
    	Vote vote = new Vote();
    	vote.setNodeId("0x1");
    	vote.setProposalHash("0x123124");
    	vote.setOption(1);
    	vote.setHash("0x");
    	votes.add(vote);
    	when(voteMapper.selectByExample(any())).thenReturn(votes);
    	Proposal proposal = new Proposal();
    	proposal.setHash("0x");
    	proposal.setStatus(CustomProposal.StatusEnum.CANCEL.getCode());
    	when(proposalMapper.selectByPrimaryKey(any())).thenReturn(proposal);
    	target.setSlashParameters("0x1");
    	proposal.setStatus(CustomProposal.StatusEnum.VOTING.getCode());
    	when(proposalMapper.selectByPrimaryKey(any())).thenReturn(proposal);
    	target.setSlashParameters("0x1");
    }
}
