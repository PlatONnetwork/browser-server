package com.platon.browser.exception;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ContractInvokeExceptionTest {

	@Test
	public void testContractInvokeException() {
		try {
			throw new ContractInvokeException("ContractInvokeException");
		} catch (Exception e) {
			assertTrue(e instanceof ContractInvokeException);;
		}
	}

}
