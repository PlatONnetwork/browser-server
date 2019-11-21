package com.platon.browser.dto;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.platon.browser.dto.CustomSlash.StatusEnum;
import com.platon.browser.dto.CustomSlash.YesNoEnum;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomSlashTest {

	private CustomSlash slash;

	@Test
	public void testCustomSlash() {
		slash = new CustomSlash();
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
