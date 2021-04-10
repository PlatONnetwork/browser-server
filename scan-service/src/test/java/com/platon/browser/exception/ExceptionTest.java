package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExceptionTest {

	@Test
	public void testBlankResponseExceptionString() {
		BlankResponseException be = new BlankResponseException("BlankResponseException");
		assertTrue(be.getMessage().equals("BlankResponseException"));
		try {
			throw be;
		} catch (Exception e) {
			assertTrue(e instanceof BlankResponseException);;
		}
	}

	@Test
	public void testNetworkExceptionString() {
		NetworkException be = new NetworkException("NetworkException");
		assertTrue(be.getMessage().equals("NetworkException"));
		try {
			throw be;
		} catch (Exception e) {
			assertTrue(e instanceof NetworkException);;
		}
	}
	
}
