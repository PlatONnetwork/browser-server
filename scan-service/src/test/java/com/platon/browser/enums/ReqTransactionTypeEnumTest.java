package com.platon.browser.enums;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ReqTransactionTypeEnumTest {

	@Test
	public void test() {
		ReqTransactionTypeEnum typeEnum = ReqTransactionTypeEnum.valueOf("TRANSACTION_DELEGATE");
		assertTrue(typeEnum.getCode().equals("1"));
		assertTrue(typeEnum.getDescription().equals("委托"));
	}

	@Test
	public void testGetTxType() {
		List<Object> list = ReqTransactionTypeEnum.getTxType("transfer");
		assertTrue(list.size()>0);
//		delegate
		list = ReqTransactionTypeEnum.getTxType("delegate");
		assertTrue(list.size()>0);
//		staking
		list = ReqTransactionTypeEnum.getTxType("staking");
		assertTrue(list.size()>0);
//		proposal
		list = ReqTransactionTypeEnum.getTxType("proposal");
		assertTrue(list.size()>0);
	}

}
