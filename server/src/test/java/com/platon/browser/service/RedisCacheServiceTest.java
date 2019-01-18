package com.platon.browser.service;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.dto.StatisticsCache;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.util.DataGenTool;
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
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private String nodeCacheKeyTemplate;
    @Value("${platon.redis.key.max-item}")
    protected long maxItemNum;

    public static class CacheKey {
        public CacheKey(String blockKey,String transactionKey,String nodeKey){
            this.blockKey = blockKey;
            this.transactionKey = transactionKey;
            this.nodeKey = nodeKey;
        }
        public String blockKey;
        public String transactionKey;
        public String nodeKey;
    }

    protected Map<String,CacheKey> chainIdToCacheKeyMap = new HashMap<>();

    @PostConstruct
    private void init(){
        chainsConfig.getChainIds().forEach(chainId -> {
            chainIdToCacheKeyMap.put(chainId,new CacheKey(
                    blockCacheKeyTemplate.replace("{}",chainId),
                    transactionCacheKeyTemplate.replace("{}",chainId),
                    nodeCacheKeyTemplate.replace("{}",chainId)
            ));
        });
    }

    /*************节点****************/
    @Test
    public void updateNodePushCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<NodeRanking> nodes = new HashSet<>(DataGenTool.generateNode(chainId,false));
            redisCacheService.updateNodePushCache(chainId,nodes);
            List<String> result = redisTemplate.opsForList().range(chainIdToCacheKeyMap.get(chainId).nodeKey,0,-1);
            Assert.assertEquals(nodes.size(),result.size());
        });
    }

    @Test
    public void getNodePushCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<NodeRanking> nodes = new HashSet<>(DataGenTool.generateNode(chainId,false));
            redisCacheService.updateNodePushCache(chainId,nodes);
            List<NodePushItem> result = redisCacheService.getNodePushCache(chainId);
            Assert.assertEquals(nodes.size(),result.size());
        });
    }


    /*************区块****************/
    @Test
    public void updateBlockCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Block> data = new HashSet<>(DataGenTool.generateBlock(chainId,false));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).blockKey);
            redisCacheService.updateBlockCache(chainId,data);
            Set<String> result = redisTemplate.opsForZSet().reverseRange(chainIdToCacheKeyMap.get(chainId).blockKey,0,-1);
            Assert.assertEquals(data.size(),result.size());
        });
    }

    @Test
    public void getBlockCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Block> data = new HashSet<>(DataGenTool.generateBlock(chainId,false));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).blockKey);
            redisCacheService.updateBlockCache(chainId,data);
            RespPage<BlockListItem> result = redisCacheService.getBlockPage(chainId,1,data.size());
            Assert.assertEquals(data.size(),result.getData().size());
        });
    }

    @Test
    public void getBlockPushData(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<BlockPushItem> result = redisCacheService.getBlockPushCache(chainId,1,10);
            Assert.assertEquals(10,result.size());
        });
    }


    /***************交易****************/
    @Test
    public void updateTransactionCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Transaction> data = new HashSet<>(DataGenTool.generateTransaction(chainId,false));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).transactionKey);
            redisCacheService.updateTransactionCache(chainId,data);
            Set<String> result = redisTemplate.opsForZSet().reverseRange(chainIdToCacheKeyMap.get(chainId).transactionKey,0,-1);
            Assert.assertEquals(data.size(),result.size());
        });
    }

    @Test
    public void getTransactionCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<Transaction> data = new HashSet<>(DataGenTool.generateTransaction(chainId,false));
            redisTemplate.delete(chainIdToCacheKeyMap.get(chainId).transactionKey);
            redisCacheService.updateTransactionCache(chainId,data);
            RespPage<TransactionListItem> result = redisCacheService.getTransactionPage(chainId,1,data.size());
            Assert.assertEquals(data.size(),result.getData().size());
        });
    }


    /***************统计缓存****************/
    @Test
    public void updateStatisticsCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTableAndCache();
            initBlockTableAndCache();
            initTransactionTableAndCache();
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId);
            PageHelper.startPage(1,10);
            List<NodeRanking> data = nodeRankingMapper.selectByExample(nodeRankingExample);

            TransactionListItem transaction = getOneTransaction(chainId);
            BlockKey key = new BlockKey();
            key.setChainId(chainId);
            key.setHash(transaction.getBlockHash());
            Block block = blockMapper.selectByPrimaryKey(key);

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

    @Test
    public void initCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<NodeRanking> nodes = nodeRankingMapper.selectByExample(new NodeRankingExample());
            redisCacheService.updateNodePushCache(chainId,new HashSet<>(nodes));

            List<Block> blocks = blockMapper.selectByExample(new BlockExample());
            redisCacheService.updateBlockCache(chainId,new HashSet<>(blocks));

            List<Transaction> transactions = transactionMapper.selectByExample(new TransactionExample());
            redisCacheService.updateTransactionCache(chainId,new HashSet<>(transactions));
        });

    }

    @Test
    public void pushNodes(){

        chainsConfig.getChainIds().forEach(chainId->{
            while (true){
                try {
                    List<NodeRanking> nodes = DataGenTool.getTestData(chainId,TestDataFileNameEnum.NODE,NodeRanking.class);
                    redisCacheService.updateNodePushCache(chainId,new HashSet<>(nodes));
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }
}
