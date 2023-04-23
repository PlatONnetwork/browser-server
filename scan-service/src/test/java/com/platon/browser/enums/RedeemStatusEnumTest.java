package com.platon.browser.enums;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RedeemStatusEnumTest {

	@Test
	public void test() {
		RedeemStatusEnum redeemStatusEnum = RedeemStatusEnum.valueOf("EXITING");
		assertTrue(redeemStatusEnum.getCode()==1);
	}

}
