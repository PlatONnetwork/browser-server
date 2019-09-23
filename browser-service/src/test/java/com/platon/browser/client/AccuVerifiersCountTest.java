package com.platon.browser.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AccuVerifiersCountTest {

	@Test
	public void testInit() {
		AccuVerifiersCount avc = new AccuVerifiersCount();
		avc.init("1", "2", "3", "4");
		assertTrue(avc.getAbstentions().intValue()==4);
		assertTrue(avc.getAccuVerifiers().intValue()==1);
		assertTrue(avc.getNays().intValue()==3);
		assertTrue(avc.getYeas().intValue()==2);
	}

}
