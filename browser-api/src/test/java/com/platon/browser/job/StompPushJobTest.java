package com.platon.browser.job;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.platon.browser.BrowserApiApplication;
import com.platon.browser.config.BrowserCache;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompPushJobTest {

	@Autowired
	private StompPushJob stompPushJob;
	
	@Test
	public void test_PushChainStatisticNew() {
		stompPushJob.pushChainStatisticNew();
	}
	
	@Test
	public void test_PushBlockStatisticNew() {
		stompPushJob.pushBlockStatisticNew();
	}
	
	@Test
	public void test_PushStakingListNew() {
		stompPushJob.pushStakingListNew();
	}
	
	@Test
	public void test_PushStakingStatisticNew() {
		stompPushJob.pushStakingStatisticNew();
	}
	
	@Test
	public void test_PushStakingChangeNew() {
		List<String> userList = new ArrayList<>();
		userList.add("1");
		BrowserCache.getKeys().put("1|10||all",userList);
		try {
			stompPushJob.pushStakingChangeNew();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
