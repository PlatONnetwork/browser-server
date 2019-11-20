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
}
