package com.platon;

import com.platon.browser.SpringbootApplication;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest(classes= SpringbootApplication.class, value = "spring.profiles.active=agent-1")
public class TestBase {
    @Autowired
    protected BlockFilter blockFilter;
    @Autowired
    protected NodeFilter nodeFilter;

    @Value("${chain.id}")
    protected String chainId;

    @Autowired
    protected Web3jClient web3jClient;

    @Autowired
    protected PendingFilter pendingFilter;

    @Autowired
    protected StompPushFilter stompPushFilter;

    @Autowired
    protected TransactionFilter transactionFilter;

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Value("${platon.redis.key.transaction}")
    protected String transactionCacheKeyTemplate;

}
