package com.platon.browser.service.cache;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dto.transaction.TransactionCacheDto;
import com.platon.browser.now.service.cache.StatisticCacheService;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class StatisticCacheServiceTest extends TestBase {

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
}


