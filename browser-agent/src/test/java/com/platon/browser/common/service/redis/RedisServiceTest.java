package com.platon.browser.common.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.AgentTestBase;
import com.platon.browser.common.collection.dto.CollectionNetworkStat;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisServiceTest extends AgentTestBase {
    @Mock
    protected RedisTemplate<String,String> redisTemplate;
    @Spy
    private RedisService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "redisTemplate", redisTemplate);
        ReflectionTestUtils.setField(target, "maxItemCount", 500000);
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
