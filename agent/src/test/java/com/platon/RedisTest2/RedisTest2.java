package com.platon.RedisTest2;

import com.platon.browser.SpringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class RedisTest2 {

    @Autowired
    private RedisTemplate <String, String> redisTemplate;

    protected static Logger logger = LoggerFactory.getLogger(RedisTest2.class);

    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;


    @Value("${chain.id}")
    private String chainId;

    @Test
    public void initBlockCache () {
        String obj = redisTemplate.opsForValue().get("sfsfsdfsf");
        logger.info("{}", obj);
    }

    @Test
    public void test(){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        Set<String> exist = redisTemplate.opsForZSet().reverseRange(cacheKey,0,-1);
        int a = exist.size();
        logger.info("{}",a);
    }
}

