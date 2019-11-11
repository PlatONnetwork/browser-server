package com.platon.browser.common.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.NetworkStat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 统计缓存数据处理逻辑\
 */
@Slf4j
@Service
public class RedisStatisticService implements RedisService<NetworkStat> {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /** 统计缓存信息key */
    @Value("${spring.redis.key.networkStat}")
    private String networkStatCacheKey;

    public void clear() {
        redisTemplate.delete(networkStatCacheKey);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void save(Set<NetworkStat> items) {
        if(items.isEmpty()) return;
        long startTime = System.currentTimeMillis();
        log.debug("开始更新Redis统计缓存:timestamp({})",startTime);
        // 取出缓存中的交易总数
        items.forEach(item -> redisTemplate.opsForValue().set(networkStatCacheKey,JSON.toJSONString(item)));
        long endTime = System.currentTimeMillis();
        log.debug("更新Redis统计缓存结束:timestamp({}),consume({}ms)",endTime,endTime-startTime);
    }
}
