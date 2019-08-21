package com.platon.browser.now.service.cache.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.redis.dto.TransactionRedis;
import com.platon.browser.util.I18nUtil;

@Service
public class StatisticCacheServiceImpl extends CacheBase implements StatisticCacheService {

	@Autowired
	private I18nUtil i18n;

	@Value("${platon.redis.key.blocks}")
	private String blockCacheKey;

	@Value("${platon.redis.key.transactions}")
	private String transactionCacheKey;

	@Value("${platon.redis.key.networkStat}")
	private String networkStatCacheKey;

	@Value("${platon.redis.max-item}")
	private long maxItemNum;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	public List<BlockRedis> getBlockCache(Integer pageNum, Integer pageSize) {
		CachePageInfo<Class<BlockRedis>> cpi = this.getCachePageInfo(blockCacheKey, pageNum, pageSize, BlockRedis.class, i18n,
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
		String value = redisTemplate.opsForValue().get(networkStatCacheKey);
		NetworkStatRedis networkStatRedis = JSON.parseObject(value, NetworkStatRedis.class);
		return networkStatRedis;
	}

	@Override
	public List<TransactionRedis> getTransactionCache(Integer pageNum, Integer pageSize) {
		CachePageInfo<Class<TransactionRedis>> cpi = this.getCachePageInfo(transactionCacheKey, pageNum, pageSize, TransactionRedis.class, i18n,
				redisTemplate, maxItemNum);
		List<TransactionRedis> transactionRedisList = new LinkedList<>();
		cpi.data.forEach(str -> {
			TransactionRedis transactionRedis = JSON.parseObject(str, TransactionRedis.class);
			transactionRedisList.add(transactionRedis);
		});
		return transactionRedisList;
	}

	public static void main(String[] args) {
		NetworkStatRedis networkStatRedis = new NetworkStatRedis();
		networkStatRedis.setAddIssueBegin(5000l);
		networkStatRedis.setAddIssueEnd(100l);
		networkStatRedis.setAddressQty(5);
		networkStatRedis.setBlockReward("123");
		networkStatRedis.setCreateTime(new Date());
		networkStatRedis.setCurrentNumber(100l);
		networkStatRedis.setCurrentTps(10);
		networkStatRedis.setDoingProposalQty(100);
		networkStatRedis.setId(5);
		networkStatRedis.setIssueValue("1232");
		networkStatRedis.setMaxTps(100);
		networkStatRedis.setNextSetting(6l);
		networkStatRedis.setNodeId("123456");
		networkStatRedis.setNodeName("dfdfdf");
		networkStatRedis.setProposalQty(10);
		networkStatRedis.setStakingDelegationValue("12333");
		networkStatRedis.setStakingReward("10000");
		networkStatRedis.setStakingValue("12112121");
		networkStatRedis.setTurnValue("12121212");
		networkStatRedis.setTxQty(100);
		networkStatRedis.setUpdateTime(new Date());
		System.out.println(JSONObject.toJSONString(networkStatRedis));
	}
}
