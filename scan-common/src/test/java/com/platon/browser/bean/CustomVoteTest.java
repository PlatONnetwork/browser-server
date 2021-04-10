package com.platon.browser.bean;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.platon.browser.bean.CustomVote.OptionEnum;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomVoteTest {

	@Test
	public void testCustomVote() {
		CustomVote vote = new CustomVote();
		assertNotNull(vote);
	}

	@Test
	public void testGetOptionEnum() {
		CustomVote vote = new CustomVote();
		vote.setOption(1);
	}

	@Test
	public void testOptionEnum() {
		OptionEnum em = OptionEnum.valueOf("SUPPORT");
		assertNotNull(em);
	}

}
