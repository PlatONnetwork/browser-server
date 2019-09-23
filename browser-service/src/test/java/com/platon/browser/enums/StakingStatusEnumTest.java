package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class StakingStatusEnumTest {

	@Test
	public void test() {
		StakingStatusEnum retEnum = StakingStatusEnum.valueOf("CANDIDATE");
		assertTrue(retEnum.getCode()==1);
		assertTrue(retEnum.getName().equals("candidate"));
	}
	
	@Test
	public void testGetCodeByStatus() {
		Integer code = StakingStatusEnum.getCodeByStatus(1, 1, 1);
		assertTrue(code==2);
		
		code = StakingStatusEnum.getCodeByStatus(1, 1, 2);
		assertTrue(code==1);
		
		code = StakingStatusEnum.getCodeByStatus(2, 1, 2);
		assertTrue(code==4);
		
		code = StakingStatusEnum.getCodeByStatus(3, 1, 2);
		assertTrue(code==5);
		
		code = StakingStatusEnum.getCodeByStatus(4, 1, 2);
		assertTrue(code==null);
	}

}
