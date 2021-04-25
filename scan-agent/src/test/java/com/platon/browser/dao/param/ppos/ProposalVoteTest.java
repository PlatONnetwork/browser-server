package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalVoteTest extends AgentTestBase {

    @Test
    public void test(){
        ProposalVote target = ProposalVote.builder()
                .bNum(BigInteger.ONE)
                .nodeId("null")
                .proposalHash("null")
                .stakingName("null")
                .timestamp(new Date())
                .txHash("null")
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
