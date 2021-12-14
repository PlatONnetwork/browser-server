package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.cache.TokenTransferRecordCacheDto;
import com.platon.browser.cache.TransactionCacheDto;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.ErcTypeEnum;
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
public class StatisticCacheService extends CacheBase {
	public List<Block> getBlockCache(Integer pageNum, Integer pageSize) {
		/* 分页根据key来获取数据 */
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfo(redisKeyConfig.getBlocks(), pageNum, pageSize);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	public NetworkStat getNetworkStatCache() {
		String value = redisTemplate.opsForValue().get(redisKeyConfig.getNetworkStat());
		/* 获取对象转换成统计对象 */
		NetworkStat networkStat = JSON.parseObject(value, NetworkStat.class);
		if(networkStat == null) {
			networkStat = new NetworkStat();
		}
		return networkStat;
	}

	public TransactionCacheDto getTransactionCache(Integer pageNum, Integer pageSize) {
		/* 分页根据key来获取交易数据  */
		CachePageInfo<Class<Transaction>> cpi = this.getCachePageInfo(redisKeyConfig.getTransactions(), pageNum, pageSize);
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
		CachePageInfo<Class<Block>> cpi = this.getCachePageInfoByStartEnd(redisKeyConfig.getBlocks(), start, end);
		List<Block> blockRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成区块对象 */
			Block blockRedis = JSON.parseObject(str, Block.class);
			blockRedisList.add(blockRedis);
		});
		return blockRedisList;
	}

	public TokenTransferRecordCacheDto getTokenTransferCache(Integer pageNum, Integer pageSize, ErcTypeEnum typeEnum) {
		String key = typeEnum==ErcTypeEnum.ERC20?redisKeyConfig.getErc20Tx():redisKeyConfig.getErc721Tx();
		/* 分页根据key来获取交易数据  */
		CachePageInfo<Class<ErcTx>> cpi = this.getCachePageInfo(key, pageNum, pageSize);
		List<ErcTx> oldErcTxList = new LinkedList<>();
		cpi.data.forEach(str -> {
			/* 获取数据转换成对象 */
			ErcTx tokenTransferRedis = JSON.parseObject(str, ErcTx.class);
			oldErcTxList.add(tokenTransferRedis);
		});
		return new TokenTransferRecordCacheDto(oldErcTxList, cpi.page);
	}
}
