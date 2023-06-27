package com.platon.browser.enums;

import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RetEnumTest {

	@Test
	public void test() {
		RetEnum retEnum = RetEnum.valueOf("RET_PARAM_VALLID");
		assertTrue(retEnum.getCode()==-1);
		assertTrue(retEnum.getName().equals("请求参数无效"));
	}

	@Test
	public void testGetEnumByCodeValue() {
		RetEnum retEnum = RetEnum.getEnumByCodeValue(0);
		assertTrue(retEnum.getCode()==0);

		retEnum = RetEnum.getEnumByCodeValue(10);
		assertNull(retEnum);
	}

}
