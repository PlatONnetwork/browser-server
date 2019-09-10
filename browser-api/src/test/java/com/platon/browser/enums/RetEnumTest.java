package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class RetEnumTest {

	@Test
	public void test() {
		RetEnum retEnum = RetEnum.valueOf("RET_PARAM_VALLID");
		assertTrue(retEnum.getCode()==-1);
	}
	
	@Test
	public void testGetEnumByCodeValue() {
		RetEnum retEnum = RetEnum.getEnumByCodeValue(0);
		assertTrue(retEnum.getCode()==0);
	}

}
