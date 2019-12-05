package com.platon.browser.config;

import com.platon.browser.TestBase;
import org.junit.Test;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class RedisFactoryTest extends TestBase {

    @Spy
    private RedisFactory target;

    @Test
    public void test() throws IOException {
        target.createRedisCommands();
        assertTrue(true);
    }
}
