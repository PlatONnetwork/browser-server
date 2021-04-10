package com.platon.browser.utils;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class ConvertUtilTest {

	@Test
	public void test() {
		BigInteger bigint = ConvertUtil.hexToBigInteger("1869F");
		assertTrue(bigint.intValue()==99999);
	}

}
