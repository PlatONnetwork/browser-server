package com.platon.browser.dao.param.ppos;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalCancelTest extends AgentTestBase {

    @Test
    public void test(){
        ProposalCancel target = ProposalCancel.builder()
                .blockNumber(BigInteger.ONE)
                .canceledId("0xsdfdfs")
                .description("desc")
                .endVotingBlock(BigInteger.TEN)
                .nodeId("0xdddd")
                .pIDID("dsfsf")
                .pipNum("333")
                .stakingName("name")
                .timestamp(new Date())
                .topic("33333")
                .txHash("0xdsdfsf")
                .url("wwww")
                .build();
        target.setBlockNumber(null)
        .setCanceledId(null)
        .setDescription(null)
        .setEndVotingBlock(null)
        .setNodeId(null)
        .setPIDID(null)
        .setPipNum(null)
        .setStakingName(null)
        .setTimestamp(new Date())
        .setTopic(null)
        .setTxHash(null)
        .setUrl(null);

        target.getBlockNumber();
        target.getCanceledId();
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
        target.getBusinessType();
        assertTrue(true);
    }

}
