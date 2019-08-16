package com.platon.browser.now.service.cache.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.redis.dto.TransactionRedis;
import com.platon.browser.util.I18nUtil;

@Service
public class StatisticCacheServiceImpl extends CacheBase implements StatisticCacheService {

	@Autowired
	private I18nUtil i18n;
	
	@Value("${platon.redis.key.bolck}")
	private String blockCacheKeyTemplate;
	
	@Value("${platon.redis.key.transaction}")
	private String transactionCacheKeyTemplate;
	
	@Value("${platon.redis.key.networkStat}")
	private String networkStatCacheKeyTemplate;
	
	@Value("${platon.redis.max-item}")
	private long maxItemNum;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public List<BlockRedis> getBlockCache(Integer pageNum, Integer pageSize) {
		CachePageInfo<Class<BlockRedis>> cpi = this.getCachePageInfo(blockCacheKeyTemplate, pageNum, pageSize, BlockRedis.class, i18n,
				redisTemplate, maxItemNum);
		List<BlockRedis> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			BlockRedis blockRedis = JSON.parseObject(str, BlockRedis.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	@Override
	public NetworkStatRedis getNetworkStatCache() {
		String value = redisTemplate.opsForValue().get(networkStatCacheKeyTemplate);
		NetworkStatRedis networkStatRedis = JSON.parseObject(value, NetworkStatRedis.class);
		return networkStatRedis;
	}

	@Override
	public List<TransactionRedis> getTransactionCache(Integer pageNum, Integer pageSize) {
		CachePageInfo<Class<TransactionRedis>> cpi = this.getCachePageInfo(transactionCacheKeyTemplate, pageNum, pageSize, TransactionRedis.class, i18n,
				redisTemplate, maxItemNum);
		List<TransactionRedis> transactionRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			TransactionRedis transactionRedis = JSON.parseObject(str, TransactionRedis.class);
			transactionRedisList.add(transactionRedis);
		});
		return transactionRedisList;
	}

}
