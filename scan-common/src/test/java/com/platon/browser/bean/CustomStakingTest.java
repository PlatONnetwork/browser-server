package com.platon.browser.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomStakingTest{

	private CustomStaking staking;

	@Test
	public void testEquals() {
		staking = new CustomStaking();
		assertTrue(staking.equals(new CustomStaking()));
	}
}
