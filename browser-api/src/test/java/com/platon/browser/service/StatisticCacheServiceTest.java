package com.platon.browser.service;

import com.platon.browser.ApiTestBase;
import com.platon.browser.cache.TransactionCacheDto;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;


public class StatisticCacheServiceTest extends ApiTestBase {

	@Autowired
	private StatisticCacheService statisticCacheService;
	
	

	@Test
	public void testGetBlockCache () {
		List<Block> blocks = statisticCacheService.getBlockCache(0, 10);
		assertNotNull(blocks);
	}

	@Test
	public void testGetNetworkCache () {
		NetworkStat networkstat = statisticCacheService.getNetworkStatCache();
		assertNotNull(networkstat);
	}

	@Test
	public void testGetTransactionCache () {
		TransactionCacheDto transactionCacheDto = statisticCacheService.getTransactionCache(0, 10);
		assertNotNull(transactionCacheDto);
	}
	
	@Test
	public void testGetBlockCacheByStartEnd () {
		List<Block> blocks = statisticCacheService.getBlockCacheByStartEnd(1l, 10l);
		assertNotNull(blocks);
	}
	
}


