package com.platon.browser.common.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.AgentTestBase;
import com.platon.browser.elasticsearch.dto.Block;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisBlockServiceTest extends AgentTestBase {
    @Mock
    protected RedisTemplate<String,String> redisTemplate;
    @Spy
    private RedisBlockService target;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(target, "redisTemplate", redisTemplate);
        ReflectionTestUtils.setField(target, "maxItemCount", 500000);
        ValueOperations vo = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(vo);
        ZSetOperations zo = mock(ZSetOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zo);
        when(zo.size(any())).thenReturn(100L);
        Set<String> data = new HashSet<>();
        data.add(JSON.toJSONString(blockList.get(0)));
    }

    /**
     * 根据区块号获取激励池余额
     */
    @Test
    public void test(){
        Set<Block> data = new HashSet<>(blockList);
        target.save(data,false);
        target.save(data,true);
    }
}
