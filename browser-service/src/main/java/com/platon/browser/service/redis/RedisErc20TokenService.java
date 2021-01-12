package com.platon.browser.service.redis;

import com.platon.browser.config.redis.RedisFactory;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class RedisErc20TokenService extends RedisService<Long> {

    @Resource
    private RedisFactory redisFactory;

    @Resource
    private Erc20TokenMapper erc20TokenMapper;

    @Value("${spring.redis.key.innerTx}")
    private String prefixKey;

    private String suffixKey = ":token:count";

    @Override
    public String getCacheKey() {
        return prefixKey + suffixKey;
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
        String value = redisFactory.createRedisCommands().get(getCacheKey());
        if (null == value || value.equals("")) {
            int count = erc20TokenMapper.totalErc20Token(new HashMap());
            value = String.valueOf(count);
            redisFactory.createRedisCommands().set(getCacheKey(), value);
        }
        return Long.parseLong(value);
    }
}
