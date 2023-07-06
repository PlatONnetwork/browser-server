package com.platon.browser.service.redis;

import com.platon.browser.AgentTestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AbstractRedisServiceTest extends AgentTestBase {
    @Mock
    protected RedisTemplate<String,String> redisTemplate;
    @Spy
    private AbstractRedisService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "redisTemplate", redisTemplate);
        //ReflectionTestUtils.setField(target, "maxItemCount", 500000);
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void test(){
        target.updateMinMaxScore(Collections.emptySet());
        target.updateStageSet(Collections.emptySet());
        target.updateExistScore(Collections.emptySet());
        target.getCacheKey();
        target.clear();
    }
}
