package com.platon.browser.service.util;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

import com.platon.browser.util.ConvertUtil;

public class ConvertUtilTest {

	@Test
	public void test() {
		BigInteger bigint = ConvertUtil.hexToBigInteger("1869F");
		assertTrue(bigint.intValue()==99999);
	}

}
