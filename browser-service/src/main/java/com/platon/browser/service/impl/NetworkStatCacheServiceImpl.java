package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.service.NetworkStatCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 统计缓存数据处理逻辑
 *  @file NetworkStatCacheServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class NetworkStatCacheServiceImpl implements NetworkStatCacheService {
    private final Logger logger = LoggerFactory.getLogger(NetworkStatCacheServiceImpl.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /** 统计缓存信息key */
    @Value("${platon.redis.key.networkStat}")
    private String networkStatCacheKey;

    @Override
    public void clear() {
        redisTemplate.delete(networkStatCacheKey);
    }

    @Override
    public void update( Set <NetworkStat> items) {
        long startTime = System.currentTimeMillis();
        logger.debug("开始更新Redis统计缓存:timestamp({})",startTime);
        // 取出缓存中的交易总数
        items.forEach(item -> redisTemplate.opsForValue().set(networkStatCacheKey,JSON.toJSONString(item)));
        long endTime = System.currentTimeMillis();
        logger.debug("更新Redis统计缓存结束:timestamp({}),consume({}ms)",endTime,endTime-startTime);
    }
}
