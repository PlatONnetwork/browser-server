package com.platon.browser.service.redis;

import com.platon.browser.config.redis.RedisCommands;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisTransferRecordServiceTest {
	@Mock
	private RedisTemplate<String,String> redisTemplate;
	@Spy
	@InjectMocks
	private OldRedisErc20TxService target;

	@Before
	public void setup(){
		ReflectionTestUtils.setField(target,"maxItemCount",12);
		ZSetOperations<String,String> operations = mock(ZSetOperations.class);
		when(redisTemplate.opsForZSet()).thenReturn(operations);
		when(redisTemplate.hasKey(any())).thenReturn(true);
		when(operations.size(any())).thenReturn(30L);
		//when(operations.rangeByScore(any(),any(),any())).thenReturn(Collections.EMPTY_SET);
		RedisCommands commands = mock(RedisCommands.class);
		when(commands.get(anyString())).thenReturn("90");

		ValueOperations<String,String> valueOperations = mock(ValueOperations.class);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(redisTemplate.hasKey(any())).thenReturn(true);
	}

	@Test
	public void test() {
		target.getCacheKey();
		target.updateExistScore(Collections.EMPTY_SET);
		target.updateMinMaxScore(Collections.EMPTY_SET);
		target.updateStageSet(Collections.EMPTY_SET);
	}

	
}
