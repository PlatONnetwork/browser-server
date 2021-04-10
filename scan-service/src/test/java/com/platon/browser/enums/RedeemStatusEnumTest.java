package com.platon.browser.enums;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RedeemStatusEnumTest {

	@Test
	public void test() {
		RedeemStatusEnum redeemStatusEnum = RedeemStatusEnum.valueOf("EXITING");
		assertTrue(redeemStatusEnum.getCode()==1);
	}

}
