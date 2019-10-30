//package com.platon.browser.dto;
//
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.platon.browser.dto.CustomProposal.StatusEnum;
//import com.platon.browser.dto.CustomProposal.TypeEnum;
//
//public class CustomProposalTest {
//
//	private CustomProposal proposal;
//
//	@Before
//	public void setUp() throws Exception {
//		proposal = new CustomProposal();
//	}
//
//	@Test
//	public void testCustomProposal() {
//		assertNotNull(proposal);
//	}
//
//	@Test
//	public void testGetYesList() {
//		assertNotNull(proposal.getYesList());
//	}
//
//	@Test
//	public void testGetNoList() {
//		assertNotNull(proposal.getNoList());
//	}
//
//	@Test
//	public void testGetAbstentionList() {
//		assertNotNull(proposal.getAbstentionList());
//	}
//
//	@Test
//	public void testTypeEnum() {
//		TypeEnum en = TypeEnum.valueOf("TEXT");
//		assertNotNull(en);
//	}
//
//	@Test
//	public void testStatusEnum() {
//		StatusEnum en = StatusEnum.valueOf("PASS");
//		assertNotNull(en);
//	}
//
//}
