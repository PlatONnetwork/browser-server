package com.platon.browser.utils;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NetworkParamsTest {

	@Test
	public void testNetworkParms() {
		NetworkParams networkParams = new NetworkParams();
		networkParams.setChainId("108");
		assertNotNull(NetworkParams.getChainId());
	}
}
