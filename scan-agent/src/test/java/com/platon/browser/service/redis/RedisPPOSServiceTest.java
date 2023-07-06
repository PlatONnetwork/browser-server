package com.platon.browser.service.redis;

import com.alibaba.fastjson.JSON;
import com.platon.browser.AgentTestBase;
import com.platon.browser.config.RedisKeyConfig;
import com.platon.browser.elasticsearch.dto.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisPPOSServiceTest extends AgentTestBase {

    @Mock
    protected RedisTemplate<String, String> redisTemplate;

    @Mock
    private RedisKeyConfig redisKeyConfig;

    @InjectMocks
    @Spy
    private RedisTransactionService target;

    @Before
    public void setup() {
        //ReflectionTestUtils.setField(target, "maxItemCount", 500000);
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
    public void test() {
        Set<Transaction> data = new HashSet<>(transactionList);
        target.save(data, false);
        target.save(data, true);
    }

}
