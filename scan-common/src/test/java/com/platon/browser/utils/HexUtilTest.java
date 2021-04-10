package com.platon.browser.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class HexUtilTest {

	@Test
	public void testPrefix() {
		String hexStr = HexUtil.prefix("‭3DC‬");
		assertTrue(hexStr.startsWith("0x"));
		hexStr = HexUtil.prefix("‭0x123");
		assertTrue(hexStr.startsWith("0x"));
	}
	
	@Test
	public void testAppent() {
		String hexStr = HexUtil.append("‭3DC‬");
		assertTrue(hexStr.startsWith("\t"));
	}

}
