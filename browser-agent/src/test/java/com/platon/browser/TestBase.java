package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.filters.BlockFilter;

@SpringBootTest(classes= BrowserAgentApplication.class, value = "spring.profiles.active=dev")
public class TestBase {
    @Autowired
    private PlatonClient client;
}
