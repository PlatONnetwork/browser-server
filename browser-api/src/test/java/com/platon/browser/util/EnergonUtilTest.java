package com.platon.browser.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EnergonUtilTest {

	@Test
	public void testFormatObject() {
		String str = EnergonUtil.format(12345678901234567890123456789f);
		assertTrue("12345679154391251000000000000".equals(str));
	}

	@Test
	public void testFormatObjectInteger() {
		String str = EnergonUtil.format(12345678901234567890123456789f,8);
		assertTrue("12345679154391251000000000000".equals(str));
	}

}
