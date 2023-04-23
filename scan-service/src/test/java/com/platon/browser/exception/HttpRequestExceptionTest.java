package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HttpRequestExceptionTest {

	@Test
	public void testHttpRequestException() {
		try {
			throw new HttpRequestException("HttpRequestException");
		} catch (Exception e) {
			assertTrue(e instanceof HttpRequestException);;
		}
	}

}
