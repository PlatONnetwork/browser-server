package com.platon.browser.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class HexToolTest {

	@Test
	public void testPrefix() {
		String hexStr = HexTool.prefix("‭3DC‬");
		assertTrue(hexStr.startsWith("0x"));
	}

}
