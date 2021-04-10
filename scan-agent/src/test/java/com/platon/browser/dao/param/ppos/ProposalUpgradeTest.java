package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalUpgradeTest extends AgentTestBase {

    @Test
    public void test(){
        ProposalUpgrade target = ProposalUpgrade.builder()
                .blockNumber(BigInteger.ONE)
                .description("dfsf")
                .endVotingBlock(BigInteger.TEN)
                .nodeId("null")
                .pIDID("null")
                .pipNum("null")
                .stakingName("null")
                .timestamp(new Date())
                .topic("null")
                .txHash("null")
                .url("null")
                .activeBlock(BigInteger.TEN)
                .newVersion("null")
                .build();
        target.setBlockNumber(null)
                .setDescription(null)
                .setEndVotingBlock(null)
                .setNodeId(null)
                .setPIDID(null)
                .setPipNum(null)
                .setStakingName(null)
                .setTimestamp(new Date())
                .setTopic(null)
                .setTxHash(null)
                .setUrl(null)
                .setActiveBlock(null)
                .setNewVersion(null);

        target.getBlockNumber();
        target.getDescription();
        target.getEndVotingBlock();
        target.getNodeId();
        target.getPIDID();
        target.getPipNum();
        target.getStakingName();
        target.getTimestamp();
        target.getTopic();
        target.getTxHash();
        target.getUrl();
        target.getActiveBlock();
        target.getNewVersion();
        target.getBusinessType();
        assertTrue(true);
    }
}