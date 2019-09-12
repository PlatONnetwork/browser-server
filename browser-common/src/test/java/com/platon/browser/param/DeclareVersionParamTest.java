package com.platon.browser.param;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeclareVersionParamTest {

	@Test
	public void testInit() {
		DeclareVersionParam param = new DeclareVersionParam();
		param.init("activeNode",1,"versionSigns");
		assertTrue(param.getActiveNode().equals("activeNode"));
	}

}
