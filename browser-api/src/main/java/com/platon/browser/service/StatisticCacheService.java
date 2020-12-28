package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.cache.TokenTransferRecordCacheDto;
import com.platon.browser.cache.TransactionCacheDto;
import com.platon.browser.config.redis.RedisFactory;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.util.I18nUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class StatisticCacheService extends CacheBase {

	@Resource
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

	/** 代币交易缓存key */
	@Value("${spring.redis.key.innerTx}")
	private String innerTxCacheKey;

	/** 最大item数目*/
	@Value("${spring.redis.max-item}")
	private long maxItemNum;
	
	@Resource
	private RedisFactory redisFactory;

	public List<Block> getBlockCache(Integer pageNum, Integer pageSize) {
		/* 分页根据key来获取数据 */
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfo(blockCacheKey, pageNum, pageSize, i18n, maxItemNum, redisFactory);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	public NetworkStat getNetworkStatCache() {
		String value = redisFactory.createRedisCommands().get(networkStatCacheKey);
		/* 获取对象转换成统计对象 */
		NetworkStat networkStat = JSON.parseObject(value, NetworkStat.class);
		if(networkStat == null) {
			networkStat = new NetworkStat();
		}
		return networkStat;
	}

	public TransactionCacheDto getTransactionCache(Integer pageNum, Integer pageSize) {
		/* 分页根据key来获取交易数据  */
		CachePageInfo<Class<Transaction>> cpi = this.getCachePageInfo(transactionCacheKey, pageNum, pageSize, i18n, maxItemNum, redisFactory);
		List<Transaction> transactionRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成对象 */
			Transaction transactionRedis = JSON.parseObject(str, Transaction.class);
			transactionRedisList.add(transactionRedis);
		});
		return new TransactionCacheDto(transactionRedisList, cpi.page);
	}

	public List<Block> getBlockCacheByStartEnd(Long start, Long end) {
		/* 分页根据key来获取数据 */
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfoByStartEnd(blockCacheKey, start, end, i18n, redisFactory);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	public TokenTransferRecordCacheDto getTokenTransferRecordCache(Integer pageNum, Integer pageSize) {
		/* 分页根据key来获取交易数据  */
		CachePageInfo<Class<ESTokenTransferRecord>> cpi = this.getCachePageInfo(innerTxCacheKey, pageNum, pageSize, i18n, maxItemNum, redisFactory);
		List<ESTokenTransferRecord> esTokenTransferRecordList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成对象 */
			ESTokenTransferRecord tokenTransferRedis = JSON.parseObject(str, ESTokenTransferRecord.class);
			esTokenTransferRecordList.add(tokenTransferRedis);
		});
		return new TokenTransferRecordCacheDto(esTokenTransferRecordList, cpi.page);
	}
}
