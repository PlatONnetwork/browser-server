package com.platon.browser.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisClusterConfigTest {
    @Spy
    private RedisClusterConfig target;

    @Test
    public void test() throws IOException {
        target.redisTemplate(mock(RedisConnectionFactory.class));
        assertTrue(true);
    }
}
