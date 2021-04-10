package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class BlankResponseExceptionTest {

	@Test
	public void test() {
		BlankResponseException be = new BlankResponseException("BusinessException");
		try {
			throw be;
		} catch (Exception e) {
			assertTrue(e instanceof BlankResponseException);;
		}
	}
}
