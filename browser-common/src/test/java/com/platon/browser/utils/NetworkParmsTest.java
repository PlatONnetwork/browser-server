package com.platon.browser.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NetworkParmsTest {

	@Test
	public void testNetworkParms() {
		NetworkParms networkParms = new NetworkParms();
		networkParms.setChainId("108");
		assertNotNull(NetworkParms.getChainId());
	}
}
