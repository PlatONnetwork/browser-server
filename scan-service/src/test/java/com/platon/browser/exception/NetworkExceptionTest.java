package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class NetworkExceptionTest {

	@Test
	public void test() {
		NetworkException be = new NetworkException("BusinessException");
		try {
			throw be;
		} catch (Exception e) {
			assertTrue(e instanceof NetworkException);;
		}
	}
}
