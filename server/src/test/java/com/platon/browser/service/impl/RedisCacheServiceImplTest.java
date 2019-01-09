package com.platon.browser.service.impl;

import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.service.RedisCacheService;
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
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class RedisCacheServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImplTest.class);
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

    @PostConstruct
    private void init(){
        blockCacheKey=blockCacheKeyTemplate.replace("{}",chainId);
        transactionCacheKey=transactionCacheKeyTemplate.replace("{}",chainId);
        nodeCacheKey=nodeCacheKeyTemplate.replace("{}",chainId);
    }


    /*************节点****************/
    @Test
    public void updateNodeCache(){
        Set<NodeRanking> nodes = TestDataUtil.generateNode(chainId);
        redisCacheService.updateNodeCache(chainId,nodes);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(nodeCacheKey,0,-1);
        Assert.assertEquals(nodes.size(),cache.size());
    }

    @Test
    public void getNodeCache(){
        Set<NodeRanking> nodes = TestDataUtil.generateNode(chainId);
        redisCacheService.updateNodeCache(chainId,nodes);
        List<NodePushItem> nodeInfoList = redisCacheService.getNodeList(chainId);
        Assert.assertEquals(nodes.size(),nodeInfoList.size());
    }


    /*************区块****************/
    @Test
    public void updateBlockCache(){
        Set<Block> data = TestDataUtil.generateBlock(chainId);
        redisTemplate.delete(blockCacheKey);
        redisCacheService.updateBlockCache(chainId,data);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(blockCacheKey,0,-1);
        Assert.assertEquals(data.size(),cache.size());
    }

    @Test
    public void getBlockCache(){
        Set<Block> data = TestDataUtil.generateBlock(chainId);
        redisTemplate.delete(blockCacheKey);
        redisCacheService.updateBlockCache(chainId,data);
        RespPage<BlockListItem> cache = redisCacheService.getBlockPage(chainId,1,data.size());
        Assert.assertEquals(data.size(),cache.getData().size());
    }

    @Test
    public void getBlockPushData(){
        List<BlockPushItem> blocks = redisCacheService.getBlockPushData(chainId,1,10);
        Assert.assertEquals(10,blocks.size());
    }


    /***************交易****************/

    @Test
    public void updateTransactionCache(){
        Set<Transaction> data = TestDataUtil.generateTransaction(chainId);
        redisTemplate.delete(transactionCacheKey);
        redisCacheService.updateTransactionCache(chainId,data);
        Set<String> cache = redisTemplate.opsForZSet().reverseRange(transactionCacheKey,0,-1);
        Assert.assertEquals(data.size(),cache.size());
    }

    @Test
    public void getTransactionCache(){
        Set<Transaction> data = TestDataUtil.generateTransaction(chainId);
        redisTemplate.delete(transactionCacheKey);
        redisCacheService.updateTransactionCache(chainId,data);
        RespPage<TransactionListItem> cache = redisCacheService.getTransactionPage(chainId,1,data.size());
        Assert.assertEquals(data.size(),cache.getData().size());
    }
}
