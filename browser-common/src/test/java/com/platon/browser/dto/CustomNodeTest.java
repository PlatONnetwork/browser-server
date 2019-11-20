package com.platon.browser.dto;

import com.platon.browser.dto.CustomNode.YesNoEnum;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CustomNodeTest {

	private CustomNode customNode;

	@Before
	public void setUp() throws Exception {
		customNode = new CustomNode();
	}

	@Test
	public void testCustomNode() {
		customNode.updateWithCustomStaking(new CustomStaking());
		assertNotNull(customNode);
	}

	@Test
	public void testYesNoEnum() {
		YesNoEnum en = YesNoEnum.valueOf("YES");
		assertNotNull(en);
	}

}
