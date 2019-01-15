package com.platon.browser.service;

import com.platon.browser.common.dto.StatisticsCache;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockKey;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import java.util.*;

@RunWith(SpringRunner.class)
public class RedisCacheServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTest.class);

    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    @Value("${platon.redis.key.block}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Value("${platon.redis.key.node}")
    private String pushNodeCacheKeyTemplate;
    @Value("${platon.redis.key.push-node}")
    private String nodeCacheKeyTemplate;
    @Value("${platon.redis.key.max-item}")
    protected long maxItemNum;

    public static class CacheKey {
        public CacheKey(String blockKey,String transactionKey,String nodeKey,String pushNodeKey){
            this.blockKey = blockKey;
            this.transactionKey = transactionKey;
            this.nodeKey = nodeKey;
            this.pushNodeKey = pushNodeKey;
        }
        public String blockKey;
        public String transactionKey;
        public String nodeKey;
        public String pushNodeKey;
    }

    protected Map<String,CacheKey> chainIdToCacheKeyMap = new HashMap<>();

    @PostConstruct
    private void init(){
        chainsConfig.getChainIds().forEach(chainId -> {
            chainIdToCacheKeyMap.put(chainId,new CacheKey(
                    blockCacheKeyTemplate.replace("{}",chainId),
                    transactionCacheKeyTemplate.replace("{}",chainId),
                    nodeCacheKeyTemplate.replace("{}",chainId),
                    pushNodeCacheKeyTemplate.replace("{}",chainId)
            ));
        });
    }

    /*************节点****************/
    @Test
    public void updateNodePushCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<NodeRanking> nodes = new HashSet<>(TestDataUtil.generateNode(chainId));
            redisCacheService.updateNodePushCache(chainId,nodes);
            Set<String> cache = redisTemplate.opsForZSet().reverseRange(chainIdToCacheKeyMap.get(chainId).pushNodeKey,0,-1);
            Assert.assertEquals(nodes.size(),cache.size());
        });
    }

    @Test
    public void getNodePushCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<NodeRanking> nodes = new HashSet<>(TestDataUtil.generateNode(chainId));
            redisCacheService.updateNodePushCache(chainId,nodes);
            List<NodePushItem> nodeInfoList = redisCacheService.getNodePushCache(chainId);
            Assert.assertEquals(nodes.size(),nodeInfoList.size());
        });
    }


    /*************区块****************/
    @Test
    public void updateBlockCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Block> data = new HashSet<>(TestDataUtil.generateBlock(chainId));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).blockKey);
            redisCacheService.updateBlockCache(chainId,data);
            Set<String> cache = redisTemplate.opsForZSet().reverseRange(chainIdToCacheKeyMap.get(chainId).blockKey,0,-1);
            Assert.assertEquals(data.size(),cache.size());
        });
    }

    @Test
    public void getBlockCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Block> data = new HashSet<>(TestDataUtil.generateBlock(chainId));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).blockKey);
            redisCacheService.updateBlockCache(chainId,data);
            RespPage<BlockListItem> cache = redisCacheService.getBlockPage(chainId,1,data.size());
            Assert.assertEquals(data.size(),cache.getData().size());
        });
    }

    @Test
    public void getBlockPushData(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<BlockPushItem> blocks = redisCacheService.getBlockPushCache(chainId,1,10);
            Assert.assertEquals(10,blocks.size());
        });
    }


    /***************交易****************/
    @Test
    public void updateTransactionCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Transaction> data = new HashSet<>(TestDataUtil.generateTransaction(chainId));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).transactionKey);
            redisCacheService.updateTransactionCache(chainId,data);
            Set<String> cache = redisTemplate.opsForZSet().reverseRange(chainIdToCacheKeyMap.get(chainId).transactionKey,0,-1);
            Assert.assertEquals(data.size(),cache.size());
        });
    }

    @Test
    public void getTransactionCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Transaction> data = new HashSet<>(TestDataUtil.generateTransaction(chainId));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).transactionKey);
            redisCacheService.updateTransactionCache(chainId,data);
            RespPage<TransactionListItem> cache = redisCacheService.getTransactionPage(chainId,1,data.size());
            Assert.assertEquals(data.size(),cache.getData().size());
        });
    }


    /***************统计缓存****************/
    @Autowired
    BlockMapper blockMapper;
    @Test
    public void updateStatisticsCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<NodeRanking> data = TestDataUtil.generateNode(chainId);
            BlockKey bk = new BlockKey();
            bk.setChainId(chainId);
            bk.setHash("0x0f67ebf56cbac0fc09f4dd46bfee21f2385582f9eab637c65ade2784b0669541");
            Block block = blockMapper.selectByPrimaryKey(bk);

            boolean result = redisCacheService.updateStatisticsCache(chainId,block,data);
            Assert.assertEquals(true,result);
        });
    }

    @Test
    public void getStatisticsCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            StatisticsCache result = redisCacheService.getStatisticsCache(chainId);
            Assert.assertNotNull(result);
        });
    }
}
