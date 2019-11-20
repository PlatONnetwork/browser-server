package com.platon.browser.complement.dao.param.statistic;

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

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class NetworkStatChangeTest extends AgentTestBase {

	@Test
	public void test(){
		NetworkStatChange target = NetworkStatChange.builder()
				.addIssueBegin(null)
				.addIssueEnd(null)
				.blockReward(null)
				.curNumber(null)
				.curTps(null)
				.id(null)
				.issueValue(null)
				.maxTps(null)
				.nodeId(null)
				.nextSettle(null)
				.nodeName(null)
				.nodeOptSeq(null)
				.proposalQty(null)
				.stakingReward(null)
				.turnValue(null)
				.txQty(null)
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
