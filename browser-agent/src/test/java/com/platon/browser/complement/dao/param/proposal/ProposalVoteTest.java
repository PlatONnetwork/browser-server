package com.platon.browser.complement.dao.param.proposal;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalVoteTest extends AgentTestBase {

    @Test
    public void test(){
        ProposalVote target = ProposalVote.builder()
                .bNum(null)
                .nodeId(null)
                .proposalHash(null)
                .stakingName(null)
                .timestamp(null)
                .txHash(null)
                .voteOption(1)
                .build();
        target.setBNum(null)
                .setNodeId(null)
                .setProposalHash(null)
                .setStakingName(null)
                .setTimestamp(null)
                .setTxHash(null)
                .setVoteOption(1);

        target.getBNum();
        target.getNodeId();
        target.getProposalHash();
        target.getStakingName();
        target.getTimestamp();
        target.getTxHash();
        target.getVoteOption();
        target.getBusinessType();
        assertTrue(true);
    }
}
