package com.platon.browser.exception;

import static org.junit.Assert.*;

import org.junit.Test;

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
