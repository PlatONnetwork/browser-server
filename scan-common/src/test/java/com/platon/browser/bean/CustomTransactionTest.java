//package com.platon.browser.dto;
//
//import static org.junit.Assert.*;
//
//import java.util.Map;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.platon.browser.dto.CustomTransaction.TxReceiptStatusEnum;
//import com.platon.browser.dto.CustomTransaction.TxTypeEnum;
//
//public class CustomTransactionTest {
//
//	private CustomTransaction ts;
//
//	@Before
//	public void setUp() throws Exception {
//		ts = new CustomTransaction();
//	}
//
//	@Test
//	public void testCustomTransaction() {
//		assertNotNull(ts);
//	}
//
//	@Test
//	public void testGetTypeEnum() {
//		ts.setTxType("0");
//		TxTypeEnum em = ts.getTypeEnum();
//		assertNotNull(em);
//	}
//
//	@Test
//	public void testGetTxParam() {
//		ts.setTxInfo("{\"key1\":\"value1\"}");
//		Map<?,?> map = ts.getTxParam(Map.class);
//		assertNotNull(map);
//	}
//
//	@Test
//	public void testTxTypeEnum() {
//		TxTypeEnum em = TxTypeEnum.valueOf("DECLARE_VERSION");
//		assertNotNull(em);
//	}
//
//	@Test
//	public void testTxReceiptStatusEnum() {
//		TxReceiptStatusEnum em = TxReceiptStatusEnum.valueOf("SUCCESS");
//		assertNotNull(em);
//	}
//
//}
