package com.platon.browser.enums;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StakingStatusEnumTest {

	@Test
	public void test() {
		StakingStatusEnum retEnum = StakingStatusEnum.valueOf("CANDIDATE");
		assertTrue(retEnum.getCode()==1);
		assertTrue(retEnum.getName().equals("candidate"));
		assertTrue(StakingStatusEnum.getCodeByStatus(1, 2, 2)==1);
		assertTrue(StakingStatusEnum.getCodeByStatus(2, 2, 1)==2);
		assertTrue(StakingStatusEnum.getCodeByStatus(2, 2, 2)==4);
		assertTrue(StakingStatusEnum.getCodeByStatus(3, 2, 2)==5);
	}


}
