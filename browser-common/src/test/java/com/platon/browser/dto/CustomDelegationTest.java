//package com.platon.browser.dto;
//
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.platon.browser.dto.CustomDelegation.YesNoEnum;
//
//public class CustomDelegationTest {
//
//	private CustomDelegation delegation;
//
//	@Before
//    public void setUp() {
//		delegation = new CustomDelegation();
//	}
//
//	@Test
//	public void testCustomDelegation() {
//		assertNotNull(delegation);
//	}
//
//	@Test
//	public void testDecimalDelegateHas() {
//		delegation.setDelegateHas("0");
//		assertNotNull(delegation.decimalDelegateHas());
//	}
//
//	@Test
//	public void testDecimalDelegateLocked() {
//		delegation.setDelegateLocked("65000000000000000000000");
//		assertNotNull(delegation.decimalDelegateLocked());
//	}
//
//	@Test
//	public void testDecimalDelegateReduction() {
//		delegation.setDelegateReduction("0");
//		assertNotNull(delegation.decimalDelegateReduction());
//	}
//
//	@Test
//	public void testIntegerDelegateHas() {
//		delegation.setDelegateHas("0");
//		assertNotNull(delegation.integerDelegateHas());
//	}
//
//	@Test
//	public void testIntegerDelegateLocked() {
//		delegation.setDelegateLocked("65000000000000000000000");
//		assertNotNull(delegation.integerDelegateLocked());
//	}
//
//	@Test
//	public void testIntegerDelegateReduction() {
//		delegation.setDelegateReduction("0");
//		assertNotNull(delegation.integerDelegateReduction());
//	}
//
//	@Test
//	public void testGetIsHistoryEnum() {
//		delegation.setIsHistory(1);
//		assertNotNull(delegation.getIsHistoryEnum());
//	}
//
//	@Test
//	public void testYesNoEnum() {
//		YesNoEnum enumm = YesNoEnum.getEnum(2);
//		assertNotNull(enumm);
//	}
//
//}
