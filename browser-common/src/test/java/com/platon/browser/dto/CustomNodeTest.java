package com.platon.browser.dto;

import com.platon.browser.dto.CustomNode.YesNoEnum;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CustomNodeTest {

	private CustomNode customNode;

	@Test
	public void testCustomNode() {
		customNode = new CustomNode();
		customNode.updateWithCustomStaking(new CustomStaking());
		assertNotNull(customNode);
	}

	@Test
	public void testYesNoEnum() {
		YesNoEnum en = YesNoEnum.valueOf("YES");
		assertNotNull(en);
	}

}
