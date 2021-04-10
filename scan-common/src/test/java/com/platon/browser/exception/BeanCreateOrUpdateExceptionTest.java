package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeanCreateOrUpdateExceptionTest {

	@Test
	public void testBeanCreateOrUpdateException() {
		try {
			throw new BeanCreateOrUpdateException("msg");
		} catch (Exception e) {
			assertTrue(e instanceof BeanCreateOrUpdateException);
		}
	}

}
