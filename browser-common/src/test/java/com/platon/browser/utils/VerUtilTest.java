package com.platon.browser.utils;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class VerUtilTest {

	@Test
	public void test() {
		VerUtil.toInteger("0.7.5");
		VerUtil.toVersion(BigInteger.valueOf(1567));
		VerUtil.transferBigVersion(BigInteger.valueOf(1567));
		assertTrue(true);
	}
}
