package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoSuchBeanExceptionTest {

	@Test
	public void testNoSuchBeanException() {
		try {
			throw new NoSuchBeanException("msg");
		} catch (Exception e) {
			assertTrue(e instanceof NoSuchBeanException);
		}
	}

}
