package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomProposalTest {

	private CustomProposal proposal;

	@Test
	public void testCustomProposal() {
		proposal = new CustomProposal();
		assertNotNull(proposal);
	}
}
