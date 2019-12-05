package com.platon.browser.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RedisFactoryTest {

    @Spy
    private RedisFactory target;

    @Test
    public void test() throws IOException {
        target.createRedisCommands();
        assertTrue(true);
    }
}
