package com.platon.browser.client;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccuVerifiersCountTest {

	@Test
	public void testInit() {
		AccuVerifiersCount avc = new AccuVerifiersCount();
		avc.init("1", "2", "3", "4");
		assertTrue(avc.getAbstentions().intValue()==4);
		assertTrue(avc.getAccuVerifiers().intValue()==1);
		assertTrue(avc.getNays().intValue()==3);
		assertTrue(avc.getYeas().intValue()==2);
	}
	
	@Test
	public void testRpcParam() {
		RpcParam rpcParam = new RpcParam();
		rpcParam.setId(1);
		rpcParam.setJsonrpc("2.0");
		rpcParam.setMethod("1");
		List<Long> params = new ArrayList<>();
		rpcParam.setParams(params);
		assertNotNull(rpcParam.toJsonString());
	}
	
	@Test
	public void testNodeVersion() {
		NodeVersion nodeVersion = new NodeVersion();
		nodeVersion.setBigVersion("1982");
		nodeVersion.setNodeId("0x1212");
		assertNotNull(nodeVersion);
	}

	@Test
	public void testProposalParticiantStat() {
		ProposalParticipantStat proposalParticiantStat = new ProposalParticipantStat();
		proposalParticiantStat.setVoterCount(1l);
		proposalParticiantStat.setSupportCount(1l);
		proposalParticiantStat.setOpposeCount(1l);
		proposalParticiantStat.setAbstainCount(1l);
		assertEquals(proposalParticiantStat.getVoterCount().longValue(), 1l);
		assertEquals(proposalParticiantStat.getSupportCount().longValue(), 1l);
		assertEquals(proposalParticiantStat.getOpposeCount().longValue(), 1l);
		assertEquals(proposalParticiantStat.getAbstainCount().longValue(), 1l);
	}
}
