//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomProposal;
//
//public class CustomProposalMapperTest extends TestBase {
//
//	@Autowired
//	private CustomProposalMapper customProposalMapper;
////	@Autowired
////	private ProposalMapper proposalMapper;
//
//	@Test
//	public void testSelectAll() {
//		List<CustomProposal> list = customProposalMapper.selectAll();
//		assertTrue(list.size()>=0);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		Set<Proposal> set = new HashSet<>();
//		Proposal proposal = new Proposal();
//		proposal.setHash("adfasdf");
//		proposal.setType("1");
//		proposal.setVerifier("verifier");
//		proposal.setVerifierName("verifierName");
//		proposal.setUrl("url");
//		proposal.setEndVotingBlock("endVotingBlock");
//		proposal.setYeas(0l);
//		proposal.setNays(0l);
//		proposal.setAbstentions(0l);
//		proposal.setAccuVerifiers(0l);
//		proposal.setStatus(1);
//		proposal.setPipNum("pipNum");
//		proposal.setPipId("3");
//		proposal.setBlockNumber("3");
//		set.add(proposal);
//		int num = customProposalMapper.batchInsertOrUpdateSelective(set, Proposal.Column.values());
//		assertTrue(num>=0);
//	}
//
//}
