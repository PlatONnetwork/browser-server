package com.platon.browser.enums;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorCodeEnumTest {

	@Test
	public void test() {
		ErrorCodeEnum codeEnum = ErrorCodeEnum.valueOf("DEFAULT");
		assertTrue(codeEnum.getCode()==-1);
		assertTrue(codeEnum.getDesc().equals("系统错误"));
	}

}
