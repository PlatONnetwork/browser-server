package com.platon.browser.dto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.platon.browser.dto.CustomNode.YesNoEnum;
import com.platon.browser.exception.NoSuchBeanException;

public class CustomNodeTest {
	
	private CustomNode customNode;

	@Before
	public void setUp() throws Exception {
		customNode = new CustomNode();
	}

	@Test
	public void testCustomNode() {
		assertNotNull(customNode);
	}

	@Test
	public void testGetLatestStaking() {
		try {
			CustomStaking sta = customNode.getLatestStaking();
			assertNotNull(sta);
		} catch (NoSuchBeanException e) {
			
		}
	}

	@Test
	public void testGetStakings() {
		assertNotNull(customNode.getStakings());
	}
	
	@Test
	public void testYesNoEnum() {
		YesNoEnum en = YesNoEnum.valueOf("YES");
		assertNotNull(en);
	}

}
