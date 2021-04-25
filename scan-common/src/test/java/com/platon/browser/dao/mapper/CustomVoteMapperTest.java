//package com.platon.browser.dao.mapper;
//
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.platon.browser.TestBase;
//import com.platon.browser.dto.CustomVote;
//import com.platon.browser.dto.CustomVoteProposal;
//
//public class CustomVoteMapperTest extends TestBase {
//
//	@Autowired
//	private CustomVoteMapper customVoteMapper;
//
//	@Test
//	public void testSelectAll() {
//		List<CustomVote> list = customVoteMapper.selectAll();
//		assertNotNull(list);
//	}
//
//	@Test
//	public void testBatchInsertOrUpdateSelective() {
//		Vote vote = new Vote();
//		Set<Vote> set = new HashSet<>();
//		set.add(vote);
//		Date now = new Date();
//		vote.setCreateTime(now);
//		vote.setHash("hash");
//		vote.setOption("option");
//		vote.setProposalHash("proposalHash");
//		vote.setTimestamp(now);
//		vote.setUpdateTime(now);
//		vote.setVerifier("verifier");
//		vote.setVerifierName("verifierName");
//		int num = customVoteMapper.batchInsertOrUpdateSelective(set, Vote.Column.values());
//		assertTrue(num>=0);
//	}
//
//	@Test
//	public void testSelectVotePropal() {
//		CustomVoteProposal cvp = customVoteMapper.selectVotePropal("");
//		assertNull(cvp);
//	}
//
//}
