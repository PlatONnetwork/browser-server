package com.platon.browser.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CustomNetworkStatTest {

	private CustomNetworkStat networkStat;
	
	@Before
	public void setUp() throws Exception {
		networkStat = new CustomNetworkStat();
	}

	@Test
	public void testCustomNetworkStat() {
		assertNotNull(networkStat);
	}

	@Test
	public void testDecimalBlockReward() {
		assertNotNull(networkStat.decimalBlockReward());
	}

	@Test
	public void testDecimalIssueValue() {
		assertNotNull(networkStat.decimalIssueValue());
	}

	@Test
	public void testDecimalStakingDelegationValue() {
		assertNotNull(networkStat.decimalStakingDelegationValue());
	}

	@Test
	public void testDecimalStakingReward() {
		assertNotNull(networkStat.decimalStakingReward());
	}

	@Test
	public void testDecimalStakingValue() {
		assertNotNull(networkStat.decimalStakingValue());
	}

	@Test
	public void testDecimalTurnValue() {
		assertNotNull(networkStat.decimalTurnValue());
	}

	@Test
	public void testIntegerBlockReward() {
		assertNotNull(networkStat.integerBlockReward());
	}

	@Test
	public void testIntegerIssueValue() {
		assertNotNull(networkStat.integerIssueValue());
	}

	@Test
	public void testIntegerStakingDelegationValue() {
		assertNotNull(networkStat.integerStakingDelegationValue());
	}

	@Test
	public void testIntegerStakingReward() {
		assertNotNull(networkStat.integerStakingReward());
	}

	@Test
	public void testIntegerStakingValue() {
		assertNotNull(networkStat.integerStakingValue());
	}

	@Test
	public void testIntegerTurnValue() {
		assertNotNull(networkStat.integerTurnValue());
	}

}
