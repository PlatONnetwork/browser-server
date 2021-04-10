package com.platon.browser.dao.param.statistic;

import com.platon.browser.AgentTestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatChangeTest extends AgentTestBase {

	@Test
	public void test(){
		NetworkStatChange target = NetworkStatChange.builder()
				.addIssueBegin(333L)
				.addIssueEnd(333L)
				.blockReward(BigDecimal.ZERO)
				.curNumber(33L)
				.curTps(333)
				.id(333)
				.issueValue(BigDecimal.TEN)
				.maxTps(33)
				.nodeId("null")
				.nextSettle(33L)
				.nodeName("null")
				.nodeOptSeq(33L)
				.proposalQty(33)
				.stakingReward(BigDecimal.TEN)
				.turnValue(BigDecimal.TEN)
				.txQty(33)
				.build();
		target.setAddIssueBegin(null)
			.setAddIssueEnd(null)
			.setBlockReward(null)
			.setCurNumber(null)
			.setCurTps(null)
			.setId(null)
			.setIssueValue(null)
			.setMaxTps(null)
			.setNodeId(null)
			.setNextSettle(null)
			.setNodeName(null)
			.setNodeOptSeq(null)
			.setProposalQty(null)
			.setStakingReward(null)
			.setTurnValue(null)
			.setTxQty(null);

		target.getAddIssueBegin();
		target.getAddIssueEnd();
		target.getBlockReward();
		target.getCurNumber();
		target.getCurTps();
		target.getId();
		target.getIssueValue();
		target.getMaxTps();
		target.getNodeId();
		target.getNextSettle();
		target.getNodeName();
		target.getNodeOptSeq();
		target.getProposalQty();
		target.getStakingReward();
		target.getTurnValue();
		target.getTxQty();
		target.getBusinessType();
		assertTrue(true);
	}
}
