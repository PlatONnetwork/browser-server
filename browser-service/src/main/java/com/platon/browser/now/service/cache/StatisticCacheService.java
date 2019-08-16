package com.platon.browser.now.service.cache;

import java.util.List;

import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.redis.dto.TransactionRedis;

public interface StatisticCacheService {
	
	/**
	 * 分页获取区块缓存数据
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<BlockRedis> getBlockCache(Integer pageNum, Integer pageSize);
	
	public NetworkStatRedis getNetworkStatCache();
	
	/**
	 * 分页获取交易缓存数据
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<TransactionRedis> getTransactionCache(Integer pageNum, Integer pageSize);
}
