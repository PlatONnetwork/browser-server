package com.platon.browser.service.redis;

import com.platon.browser.dao.entity.NetworkStat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 统计缓存数据处理逻辑\
 */
@Service
public class RedisStatisticService extends RedisService<NetworkStat> {
    /** 统计缓存信息key */
    @Value("${spring.redis.key.networkStat}")
    private String networkStatCacheKey;
    @Override
    public String getCacheKey() {
        return networkStatCacheKey;
    }
}
