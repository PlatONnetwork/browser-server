package com.platon.browser.enums;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StakingStatusEnumTest {

	@Test
	public void test() {
		StakingStatusEnum retEnum = StakingStatusEnum.valueOf("CANDIDATE");
		assertTrue(retEnum.getCode()==1);
		assertTrue(retEnum.getName().equals("candidate"));
	}


}
