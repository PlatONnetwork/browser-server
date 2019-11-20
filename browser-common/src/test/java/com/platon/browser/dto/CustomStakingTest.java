package com.platon.browser.dto;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CustomStakingTest{

	private CustomStaking staking;

	@Test
	public void testEquals() {
		staking = new CustomStaking();
		assertTrue(staking.equals(new CustomStaking()));
	}
}
