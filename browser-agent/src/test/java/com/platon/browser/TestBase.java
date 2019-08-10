package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.TransactionFilter;
import com.platon.browser.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@SpringBootTest(classes= BrowserAgentApplication.class, value = "spring.profiles.active=dev")
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
    protected TransactionFilter transactionFilter;

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;


    @Autowired
    protected TransactionMapper transactionMapper;

    @Autowired
    protected DBService dbService;


    @Value("${platon.redis.key.transaction}")
    protected String transactionCacheKeyTemplate;

    protected Web3j web3j;

    @PostConstruct
    private void init(){
        web3j = chainsConfig.getWeb3j(chainId);
    }

}
