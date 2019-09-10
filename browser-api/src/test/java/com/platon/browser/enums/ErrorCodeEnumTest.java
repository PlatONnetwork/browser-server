package com.platon.browser.enums;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorCodeEnumTest {

	@Test
	public void test() {
		ErrorCodeEnum codeEnum = ErrorCodeEnum.valueOf("Default");
		assertTrue(codeEnum.getCode()==-1);
	}

}
