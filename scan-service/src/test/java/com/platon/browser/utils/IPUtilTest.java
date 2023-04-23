package com.platon.browser.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IPUtilTest {

	@Test
	public void testIsIPv4Address() {
		assertTrue(IPUtil.isIPv4Address("10.10.8.61"));
	}

	@Test
	public void testIsIPv6StdAddress() {
		assertTrue(IPUtil.isIPv6StdAddress("2001:0db8:85a3:08d3:1319:8a2e:0370:7344"));
	}

	@Test
	public void testIsIPv6HexCompressedAddress() {
		assertTrue(IPUtil.isIPv6HexCompressedAddress("ADBF:0:FEEA::EA:AC:DEED"));
	}

	@Test
	public void testIsIPv6Address() {
		assertTrue(IPUtil.isIPv6Address("2001:0db8:85a3:08d3:1319:8a2e:0370:7344"));
		assertTrue(IPUtil.isIPv6Address("ADBF:0:FEEA::EA:AC:DEED"));
	}

}
