package com.platon.browser.service;

import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class BlockServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);
    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    protected String blockCacheKey;
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    protected String transactionCacheKey;
    @Value("${platon.redis.key.node}")
    private String nodeCacheKeyTemplate;
    protected String nodeCacheKey;
    @Value("${platon.redis.key.max-item}")
    protected long maxItemNum;

    protected String chainId = "1";

    @Autowired
    private BlockMapper blockMapper;

    @PostConstruct
    private void init(){
        blockCacheKey=blockCacheKeyTemplate.replace("{}",chainId);
        transactionCacheKey=transactionCacheKeyTemplate.replace("{}",chainId);
        nodeCacheKey=nodeCacheKeyTemplate.replace("{}",chainId);
    }

    /*************区块****************/
    @Test
    public void insertBlock(){
        List<Block> data = TestDataUtil.generateBlock(chainId);
        int actualCount = blockMapper.batchInsert(data);
        Assert.assertEquals(data.size(),actualCount);
    }

}
