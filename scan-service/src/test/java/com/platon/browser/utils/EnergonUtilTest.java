package com.platon.browser.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EnergonUtilTest {

	@Test
	public void testFormatObject() {
		String str = EnergonUtil.format(12345678901234567890123456789f);
		assertTrue("12345679154391251000000000000".equals(str));
	}

	@Test
	public void testFormatObjectInteger() {
		String str;
		try {
			str = EnergonUtil.format("abc",8);
		} catch (Exception e) {
			assertTrue(e instanceof NumberFormatException);
		}

		str = EnergonUtil.format(12345678901234567890123456789f,8);
		assertTrue("12345679154391251000000000000".equals(str));

		str = EnergonUtil.format(0,8);
		assertTrue("0".equals(str));

		str = EnergonUtil.format(0.1,8);
		assertTrue("0.1".equals(str));
	}

}
