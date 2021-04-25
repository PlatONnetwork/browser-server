package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

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
