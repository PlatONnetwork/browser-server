package com.platon.browser.service.redis;

import com.platon.browser.dao.entity.NetworkStat;
import org.springframework.stereotype.Service;

/**
 * 统计缓存数据处理逻辑\
 */
@Service
public class RedisStatisticService extends AbstractRedisService<NetworkStat> {
    @Override
    public String getCacheKey() {
        return redisKeyConfig.getNetworkStat();
    }
}
