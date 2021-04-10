package com.platon.browser.utils;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class ChainVersionUtilTest {

	@Test
	public void test() {
		ChainVersionUtil.toBigIntegerVersion("0.7.5");
		ChainVersionUtil.toStringVersion(BigInteger.valueOf(1567));
		ChainVersionUtil.toBigVersion(BigInteger.valueOf(1567));
		assertTrue(true);
	}
}
