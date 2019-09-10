package com.platon.browser.enums;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ReqTransactionTypeEnumTest {

	@Test
	public void test() {
		ReqTransactionTypeEnum typeEnum = ReqTransactionTypeEnum.valueOf("TRANSACTION_DELEGATE");
		assertTrue(typeEnum.getCode().equals("1"));
	}
	
	@Test
	public void testGetTxType() {
		List<String> list = ReqTransactionTypeEnum.getTxType("transfer");
		assertTrue(list.size()>0);
	}

}
