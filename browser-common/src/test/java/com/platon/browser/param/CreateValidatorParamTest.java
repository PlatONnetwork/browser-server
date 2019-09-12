package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CreateValidatorParamTest {
	
	private CreateValidatorParam param;
	
	@Before
	public void setUp() throws Exception {
		param = new CreateValidatorParam();
		param.init(1,"benefitAddress",
				"nodeId","externalId","nodeName",
				"website","details","1000000000000000000","0.7.0");
	}
	
	@Test
	public void testInit() {
		assertTrue(param.getBenefitAddress().equals("benefitAddress"));
	}

	@Test
	public void testDecimalAmount() {
		assertTrue(param.decimalAmount().longValue()==1000000000000000000l);
	}

	@Test
	public void testIntegerAmount() {
		assertTrue(param.integerAmount().longValue()==1000000000000000000l);
	}

}
