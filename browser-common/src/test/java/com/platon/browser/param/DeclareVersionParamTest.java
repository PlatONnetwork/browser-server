package com.platon.browser.param;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DeclareVersionParamTest {

	@Test
	public void testInit() {
		VersionDeclareParam param = VersionDeclareParam.builder()
                .activeNode("sss")
                .nodeName("fdfdsf")
                .version(333)
                .versionSigns("dfdsfs")
                .build();
		assertTrue(param.getActiveNode().equals("sss"));
	}

}
