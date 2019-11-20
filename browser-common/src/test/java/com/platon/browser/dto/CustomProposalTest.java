package com.platon.browser.dto;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CustomProposalTest {

	private CustomProposal proposal;

	@Test
	public void testCustomProposal() {
		proposal = new CustomProposal();
		assertNotNull(proposal);
	}
}
