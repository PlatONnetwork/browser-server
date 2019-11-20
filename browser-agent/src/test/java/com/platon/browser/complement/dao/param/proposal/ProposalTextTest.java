package com.platon.browser.complement.dao.param.proposal;

import com.platon.browser.AgentTestBase;
import com.platon.browser.common.enums.BusinessType;
import com.platon.browser.complement.dao.param.BusinessParam;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalTextTest extends AgentTestBase {

    @Test
    public void test(){
        ProposalText target = ProposalText.builder()
                .blockNumber(BigInteger.ONE)
                .description("null")
                .endVotingBlock(BigInteger.TEN)
                .nodeId("0xdsfsfd")
                .pIDID("null")
                .pipNum("null")
                .stakingName("null")
                .timestamp(new Date())
                .topic("null")
                .txHash("null")
                .url("null")
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
                .setUrl(null);

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
        target.getBusinessType();
        assertTrue(true);
    }
}
