package com.platon.browser.service.redis;

import com.platon.browser.config.RedisKeyConfig;
import com.platon.browser.config.redis.JedisClient;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class RedisErc20TokenService extends AbstractRedisService<Long> {
    @Resource
    private JedisClient jedisClient;

    @Resource
    private Erc20TokenMapper erc20TokenMapper;

    private String suffixKey = ":token:count";

    @Override
    public String getCacheKey() {
        return redisKeyConfig.getInnerTx() + suffixKey;
    }

    private boolean isInit = false;
    private long lastCheckTime = 0;

    public void addTokenCount(long increment) {
        long currentTime = System.currentTimeMillis();
        if (!isInit || lastCheckTime == 0 || (currentTime - lastCheckTime > 36000)) {
            int count = erc20TokenMapper.totalErc20Token(new HashMap());
            redisTemplate.opsForValue().set(getCacheKey(), count + "");
            isInit = true;
            lastCheckTime = System.currentTimeMillis();
        }
        redisTemplate.opsForValue().increment(getCacheKey(), increment);
    }

    public long getTokenCount() {
        String value = jedisClient.get(getCacheKey());
        if (null == value || value.equals("")) {
            int count = erc20TokenMapper.totalErc20Token(new HashMap());
            value = String.valueOf(count);
            jedisClient.set(getCacheKey(), value);
        }
        return Long.parseLong(value);
    }
}
