package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReceiveTypeEnumTest {

	@Test
	public void testReceiveTypeEnum() {
		ReceiveTypeEnum enumm = ReceiveTypeEnum.ACCOUNT;
		assertNotNull(enumm);
	}

	@Test
	public void testGetCode() {
		ReceiveTypeEnum enumm = ReceiveTypeEnum.ACCOUNT;
		assertNotNull(enumm.getCode());
	}

	@Test
	public void testGetDesc() {
		ReceiveTypeEnum enumm = ReceiveTypeEnum.ACCOUNT;
		assertNotNull(enumm.getDesc());
	}

}
