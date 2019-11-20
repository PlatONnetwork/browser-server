package com.platon.browser.dto;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.dto.CustomStaking.YesNoEnum;

public class CustomStakingTest {

	private CustomStaking staking;

	@Before
	public void setUp() throws Exception {
		staking = new CustomStaking();
	}

	@Test
	public void testEquals() {
		assertTrue(staking.equals(new CustomStaking()));
	}

	@Test
	public void testHashCode() {
		assertNotNull(staking.hashCode());
	}

	@Test
	public void testCustomStaking() {
		assertNotNull(staking);
	}



	@Test
	public void testStatusEnum() {
		StatusEnum en = StatusEnum.valueOf("CANDIDATE");
		assertNotNull(en);
	}

	@Test
	public void testYesNoEnum() {
		YesNoEnum en = YesNoEnum.valueOf("YES");
		assertNotNull(en);
	}

}
