package com.platon.RedisTest2;

import com.platon.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
public class RedisTest2 extends TestBase {

    protected static Logger logger = LoggerFactory.getLogger(RedisTest2.class);

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

