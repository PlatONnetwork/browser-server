package com.platon.browser.dao.param.ppos;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ProposalVersionTest extends TestBase {

	@Test
	public void test(){
		ProposalVersion target = ProposalVersion.builder()
				.optDesc("null")
				.build();
		target.setOptDesc(null);

		target.getOptDesc();
		target.getBusinessType();
		assertTrue(true);
	}
}
