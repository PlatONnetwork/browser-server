package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
