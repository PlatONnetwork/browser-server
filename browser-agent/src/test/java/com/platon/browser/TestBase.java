package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.PendingFilter;
import com.platon.browser.filter.TransactionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@SpringBootTest(classes= AgentApplication.class, value = "spring.profiles.active=dev")
public class TestBase {
    @Autowired
    protected BlockFilter blockFilter;
    //@Autowired
    //protected NodeFilter nodeFilter;

    @Value("${platon.chain.active}")
    protected String chainId;

    @Autowired
    protected PlatonClient web3jClient;
    @Autowired
    protected ChainsConfig chainsConfig;

    @Autowired
    protected PendingFilter pendingFilter;

    @Autowired
    protected TransactionFilter transactionFilter;

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    protected NodeRankingMapper nodeRankingMapper;

    @Value("${platon.redis.key.transaction}")
    protected String transactionCacheKeyTemplate;

    protected Web3j web3j;

    @PostConstruct
    private void init(){
        web3j = chainsConfig.getWeb3j(chainId);
    }

}
