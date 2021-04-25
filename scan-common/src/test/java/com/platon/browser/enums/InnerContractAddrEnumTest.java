package com.platon.browser.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class InnerContractAddrEnumTest {

	@Test
	public void testInnerContractAddrEnum() {
		InnerContractAddrEnum enumm = InnerContractAddrEnum.DELEGATE_CONTRACT;
		assertNotNull(enumm);
	}

	@Test
	public void testGetAddress() {
		InnerContractAddrEnum enumm = InnerContractAddrEnum.DELEGATE_CONTRACT;
		assertNotNull(enumm.getAddress());
	}

	@Test
	public void testGetDesc() {
		InnerContractAddrEnum enumm = InnerContractAddrEnum.DELEGATE_CONTRACT;
		assertNotNull(enumm.getDesc());
	}

}
