package com.platon.browser.service.redis;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.BrowserServiceApplication;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= BrowserServiceApplication.class, value = "spring.profiles.active=test")
public class RedisBlockServiceTest {

	@Autowired
	private RedisBlockService redisBlockService;

	@Autowired
	private RedisTransactionService redisTransactionService;

	@Test
	public void testRedisBlock() {
		redisBlockService.getCacheKey();
	}

	@Test
	public void testBlock() {
		List<Block> blocks = new ArrayList<>();
		Block block = new Block();
		block.setNum(1l);
		blocks.add(block);
		redisBlockService.save(new HashSet<>(blocks),false);
		redisBlockService.updateStageSet(new HashSet<>(blocks));
		Set<String> set = new HashSet<>();
		set.add(JSONObject.toJSONString(block));
		redisBlockService.updateExistScore(set);
		Block block1 = new Block();
		block1.setNum(2l);
		blocks.add(block1);
		redisBlockService.updateMinMaxScore(new HashSet<>(blocks));
		redisBlockService.clear();
	}

	@Test
	public void testTransaction() {
		List<Transaction> transactions = new ArrayList<>();
		Transaction transaction = new Transaction();
		transaction.setHash("0x11");
		transaction.setNum(1l);
		transaction.setSeq(100001l);
		transaction.setType(1);
		transactions.add(transaction);
		redisTransactionService.save(new HashSet<>(transactions),false);
		redisTransactionService.updateStageSet(new HashSet<>(transactions));
		Set<String> set = new HashSet<>();
		set.add(JSONObject.toJSONString(transaction));
		redisTransactionService.updateExistScore(set);
		Transaction transaction1 = new Transaction();
		transaction1.setHash("0x22");
		transaction1.setNum(2l);
		transaction.setType(1);
		transaction.setSeq(100002l);
		transactions.add(transaction1);
		redisTransactionService.clear();
	}

}
