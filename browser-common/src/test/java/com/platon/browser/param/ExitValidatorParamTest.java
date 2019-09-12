package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ExitValidatorParamTest {

	@Test
	public void testInit() {
		ExitValidatorParam param = new ExitValidatorParam();
		param.init("nodeId","nodeName","1024");
		assertTrue("nodeId".equals(param.getNodeId()));
	}

}
