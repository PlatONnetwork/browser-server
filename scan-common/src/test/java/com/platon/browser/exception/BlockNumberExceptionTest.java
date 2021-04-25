package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockNumberExceptionTest {

	@Test
	public void testBlockNumberException() {
		try {
			throw new BlockNumberException("msg");
		} catch (Exception e) {
			assertTrue(e instanceof BlockNumberException);
		}
	}

}
