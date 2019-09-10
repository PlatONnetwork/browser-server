package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class StakingStatusEnumTest {

	@Test
	public void test() {
		StakingStatusEnum retEnum = StakingStatusEnum.valueOf("CANDIDATE");
		assertTrue(retEnum.getCode()==1);
	}
	
	@Test
	public void testGetCodeByStatus() {
		Integer code = StakingStatusEnum.getCodeByStatus(1, 1, 1);
		assertTrue(code==2);
	}

}
