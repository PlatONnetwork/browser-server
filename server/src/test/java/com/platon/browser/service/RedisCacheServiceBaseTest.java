package com.platon.browser.service;

import com.platon.browser.ServerApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class RedisCacheServiceBaseTest {
    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    protected String blockCacheKey;
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    protected String transactionCacheKey;
    @Value("${platon.redis.key.node}")
    private String nodeCacheKeyTemplate;
    protected String nodeCacheKey;
    @Value("${platon.redis.key.max-item}")
    protected long maxItemNum;

    protected String chainId = "1";

    @PostConstruct
    private void init(){
        blockCacheKey=blockCacheKeyTemplate.replace("{}",chainId);
        transactionCacheKey=transactionCacheKeyTemplate.replace("{}",chainId);
        nodeCacheKey=nodeCacheKeyTemplate.replace("{}",chainId);
    }
}
