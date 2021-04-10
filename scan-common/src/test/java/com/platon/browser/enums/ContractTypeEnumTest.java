package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

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
		enumm = ContractTypeEnum.UNKNOWN;
		assertNotNull(enumm);
	}

}
