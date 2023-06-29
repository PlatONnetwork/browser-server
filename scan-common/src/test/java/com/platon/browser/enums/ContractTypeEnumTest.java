package com.platon.browser.enums;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class ContractTypeEnumTest {

	@Test
	public void testContractTypeEnumTest() {
		ContractTypeEnum enumm = ContractTypeEnum.EVM;
		assertNotNull(enumm);
		assertNotNull(enumm.getCode());
		assertNotNull(enumm.getDesc());
		enumm = ContractTypeEnum.INNER;
		assertNotNull(enumm);
		enumm = ContractTypeEnum.WASM;
		assertNotNull(enumm);
	}

}
