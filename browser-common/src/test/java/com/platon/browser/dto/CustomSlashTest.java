package com.platon.browser.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.platon.browser.dto.CustomSlash.StatusEnum;
import com.platon.browser.dto.CustomSlash.YesNoEnum;

public class CustomSlashTest {

	private CustomSlash slash;

	@Before
	public void setUp() throws Exception {
		slash = new CustomSlash();
	}

	@Test
	public void testCustomSlash() {
		assertNotNull(slash);
	}

	@Test
	public void testStatusEnum() {
		StatusEnum en = StatusEnum.valueOf("SUCCESS");
		assertNotNull(en);
	}

	@Test
	public void testYesNoEnum() {
		YesNoEnum en = YesNoEnum.valueOf("YES");
		assertNotNull(en);
	}

}
