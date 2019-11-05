package com.platon.browser.now.service.cache.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.transaction.TransactionCacheDto;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.util.I18nUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取缓存逻辑具体实现
 *  @file StatisticCacheServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class StatisticCacheServiceImpl extends CacheBase implements StatisticCacheService {

	@Autowired
	private I18nUtil i18n;

	/** 区块缓存key */
	@Value("${spring.redis.key.blocks}")
	private String blockCacheKey;

	/** 交易缓存key */
	@Value("${spring.redis.key.transactions}")
	private String transactionCacheKey;

	/** 统计缓存key */
	@Value("${spring.redis.key.networkStat}")
	private String networkStatCacheKey;

	/** 最大item数目*/
	@Value("${spring.redis.max-item}")
	private long maxItemNum;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public List<Block> getBlockCache( Integer pageNum, Integer pageSize) {
		/** 分页根据key来获取数据 */
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfo(blockCacheKey, pageNum, pageSize, Block.class, i18n,
				redisTemplate, maxItemNum);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/** 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	@Override
	public NetworkStat getNetworkStatCache() {
		String value = redisTemplate.opsForValue().get(networkStatCacheKey);
		/** 获取对象转换成统计对象 */
		return JSON.parseObject(value, NetworkStat.class);
	}

	@Override
	public TransactionCacheDto getTransactionCache(Integer pageNum, Integer pageSize) {
		/** 分页根据key来获取交易数据  */
		CachePageInfo<Class<Transaction>> cpi = this.getCachePageInfo(transactionCacheKey, pageNum, pageSize, Transaction.class, i18n,
				redisTemplate, maxItemNum);
		List<TransactionWithBLOBs> transactionRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/** 获取数据转换成对象 */
			TransactionWithBLOBs transactionRedis = JSON.parseObject(str, TransactionWithBLOBs.class);
			transactionRedisList.add(transactionRedis);
		});
		return new TransactionCacheDto(transactionRedisList, cpi.page);
	}

	@Override
	public List<Block> getBlockCacheByStartEnd(Long start, Long end) {
		/** 分页根据key来获取数据 */
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfoByStartEnd(blockCacheKey, start, end, Block.class, i18n,
				redisTemplate, maxItemNum);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/** 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

}
