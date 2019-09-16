package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EditValidatorParamTest {

	@Test
	public void testInit() {
		EditValidatorParam param = new EditValidatorParam();
		param.init("benefitAddress","nodeId","externalId","nodeName","website","details");
		assertTrue("benefitAddress".equals(param.getBenefitAddress()));
	}

}
