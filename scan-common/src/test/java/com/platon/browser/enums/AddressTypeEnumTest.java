package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class AddressTypeEnumTest {

	@Test
	public void testAddressTypeEnumTest() {
		AddressTypeEnum enumm = AddressTypeEnum.ACCOUNT;
		assertNotNull(enumm);
		assertNotNull(enumm.getCode());
		assertNotNull(enumm.getDesc());
		enumm = AddressTypeEnum.EVM_CONTRACT;
		assertNotNull(enumm);
		enumm = AddressTypeEnum.INNER_CONTRACT;
		assertNotNull(enumm);
		enumm = AddressTypeEnum.WASM_CONTRACT;
		assertNotNull(enumm);
	}

	@Test
	public void testGetEnums() {
		AddressTypeEnum enumm = AddressTypeEnum.getEnum(1);
		assertNotNull(enumm);
		assertTrue(AddressTypeEnum.contains(1));
		assertTrue(AddressTypeEnum.contains(AddressTypeEnum.ACCOUNT));
	}

}
