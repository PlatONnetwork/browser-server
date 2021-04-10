//package com.platon.browser.dto;
//
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.platon.browser.dto.CustomUnDelegation.StatusEnum;
//
//
//public class CustomUnDelegationTest {
//
//	private CustomUnDelegation unDelegation;
//
//	@Before
//	public void setUp() throws Exception {
//		unDelegation = new CustomUnDelegation();
//		unDelegation.setApplyAmount("0");
//		unDelegation.setRedeemLocked("0");
//	}
//
//	@Test
//	public void testCustomUnDelegation() {
//		assertNotNull(unDelegation);
//	}
//
//	@Test
//	public void testDecimalApplyAmount() {
//		assertNotNull(unDelegation.decimalApplyAmount());
//	}
//
//	@Test
//	public void testDecimalRedeemLocked() {
//		assertNotNull(unDelegation.decimalRedeemLocked());
//	}
//
//	@Test
//	public void testIntegerApplyAmount() {
//		assertNotNull(unDelegation.integerApplyAmount());
//	}
//
//	@Test
//	public void testIntegerRedeemLocked() {
//		assertNotNull(unDelegation.integerRedeemLocked());
//	}
//
//	@Test
//	public void testGetStatusEnum() {
//		unDelegation.setStatus(1);
//		assertNotNull(unDelegation.getStatusEnum());
//	}
//
//	@Test
//	public void testStatusEnum() {
//		StatusEnum em = StatusEnum.valueOf("EXITING");
//		assertNotNull(em);
//	}
//
//}
