package com.platon.browser.service.redis;

import com.platon.browser.config.RedisFactory;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RedisErc20TokenService extends RedisService<Long> {

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private Erc20TokenMapper erc20TokenMapper;

    @Value("${spring.redis.key.innerTx}")
    private String prefixKey;

    private String suffixKey = ":token:count";

    @Override
    public String getCacheKey() {
        return prefixKey + suffixKey;
    }

    private boolean isInit;

    public void addTokenCount(long increment) {
        if (!isInit) {
            int count = erc20TokenMapper.totalErc20Token(new HashMap());
            redisTemplate.opsForValue().set(getCacheKey(), count + "");
            isInit = true;
        }
        redisTemplate.opsForValue().increment(getCacheKey(), increment);
    }

    public long getTokenCount() {
        String value = redisFactory.createRedisCommands().get(getCacheKey());
        if (null == value || value.equals("")) {
            int count = erc20TokenMapper.totalErc20Token(new HashMap());
            redisFactory.createRedisCommands().set(getCacheKey(), count + "");
        }
        return Long.valueOf(redisFactory.createRedisCommands().get(getCacheKey()));
    }
}
