package com.platon.browser.bean;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomRpPlanTest {

	@Test
	public void testCustomRpPlan() {
		CustomRpPlan plan = new CustomRpPlan();
		assertNotNull(plan);
	}

}
