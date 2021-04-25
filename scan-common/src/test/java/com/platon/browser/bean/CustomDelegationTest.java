package com.platon.browser.bean;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;


public class CustomDelegationTest {

	private CustomDelegation delegation;

	@Before
    public void setUp() {
		delegation = new CustomDelegation();
	}

	@Test
	public void testCustomDelegation() {
		assertNotNull(delegation);
	}

	@Test
	public void testDecimalDelegateHas() {
	    delegation.setDelegateHes(BigDecimal.ONE);
		assertNotNull(delegation.getDelegateHes());
	}

	@Test
	public void testDecimalDelegateLocked() {
		delegation.setDelegateLocked(BigDecimal.ONE);
		assertNotNull(delegation.getDelegateLocked());
	}

	@Test
	public void testDecimalDelegateReduction() {
		delegation.setDelegateReleased(BigDecimal.ONE);
		assertNotNull(delegation.getDelegateReleased());
	}

	@Test
	public void testGetIsHistoryEnum() {
		delegation.setIsHistory(1);
		assertNotNull(delegation.getIsHistory());
	}

	@Test
	public void testYesNoEnum() {
		CustomDelegation.YesNoEnum enumm = CustomDelegation.YesNoEnum.getEnum(2);
		assertNotNull(enumm);
	}

}
