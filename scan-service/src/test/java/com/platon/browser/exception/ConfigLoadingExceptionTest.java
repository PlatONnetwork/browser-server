package com.platon.browser.exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigLoadingExceptionTest {

	@Test
	public void testConfigLoadingException() {
		try {
			throw new ConfigLoadingException("ConfigLoadingException");
		} catch (Exception e) {
			assertTrue(e instanceof ConfigLoadingException);;
		}
	}

}
