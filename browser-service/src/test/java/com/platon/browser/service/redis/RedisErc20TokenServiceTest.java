package com.platon.browser.service.redis;

import com.platon.browser.config.redis.JedisClient;
import com.platon.browser.dao.mapper.Erc20TokenMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisErc20TokenServiceTest {
	@Mock
	private JedisClient jedisClient;
	@Mock
	private Erc20TokenMapper erc20TokenMapper;
	@Mock
	private RedisTemplate<String,String> redisTemplate;
	@Spy
	@InjectMocks
	private RedisErc20TokenService target;

	@Before
	public void setup(){
		ReflectionTestUtils.setField(target,"prefixKey","prefix");
		ReflectionTestUtils.setField(target,"maxItemCount",12);
		when(erc20TokenMapper.totalErc20Token(any())).thenReturn(40);
		ZSetOperations<String,String> operations = mock(ZSetOperations.class);
		when(redisTemplate.opsForZSet()).thenReturn(operations);
		when(redisTemplate.hasKey(any())).thenReturn(true);
		when(operations.size(any())).thenReturn(30L);
		//when(operations.rangeByScore(any(),any(),any())).thenReturn(Collections.EMPTY_SET);
		when(jedisClient.get(anyString())).thenReturn("90");

		ValueOperations<String,String> valueOperations = mock(ValueOperations.class);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(redisTemplate.hasKey(any())).thenReturn(true);
	}

	@Test
	public void test() {
		target.getCacheKey();
		target.addTokenCount(55);
		target.getTokenCount();
	}

	
}
