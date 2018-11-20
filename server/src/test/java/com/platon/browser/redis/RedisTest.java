package com.platon.browser.redis;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.ServiceApplication;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    protected static Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Test
    public void initBlockCache() {
        String cacheKey = "platon:server:chain_100:blocks";

        long lastNumber = Long.MAX_VALUE;
        for(int i=0;i<500;i++){

            BlockExample condition = new BlockExample();
            condition.createCriteria().andChainIdEqualTo("100").andNumberLessThan(lastNumber);
            condition.setOrderByClause("number desc");

            PageHelper.startPage(i+1,1000);
            List<Block> blockList = blockMapper.selectByExample(condition);
            Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
            blockList.forEach(block -> {
                Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,block.getNumber(),block.getNumber());
                if(exist.size()==0){
                    // 在缓存中不存在的才放入缓存
                    tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(block),block.getNumber().doubleValue()));
                }
            });
            if(tupleSet.size()>0){
                redisTemplate.opsForZSet().add(cacheKey, tupleSet);
            }

            int index = blockList.size()-1;
            if(index>=0){
                Block lastBlock = blockList.get(index);
                if(lastBlock!=null){
                    lastNumber = lastBlock.getNumber();
                }
            }
        }
    }

    @Test
    public void initTransactionCache() {

        String cacheKey = "platon:server:chain_100:transactions";

        TransactionExample condition = new TransactionExample();
        condition.createCriteria().andChainIdEqualTo("100");
        condition.setOrderByClause("block_number desc,transaction_index desc");
        for(int i=0;i<500;i++){
            PageHelper.startPage(i+1,1000);
            List<Transaction> transactionList = transactionMapper.selectByExample(condition);
            Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
            transactionList.forEach(transaction -> {
                Long value = transaction.getBlockNumber()+transaction.getTransactionIndex();
                Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,value,value);
                if(exist.size()==0){
                    // 在缓存中不存在的才放入缓存
                    tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(transaction),value.doubleValue()));
                }
            });
            if(tupleSet.size()>0){
                redisTemplate.opsForZSet().add(cacheKey, tupleSet);
            }
        }
    }

    @Test
    public void pageBlockCache(){
        logger.info("第1页=============");
        Set<String> blockSet = redisTemplate.opsForZSet().reverseRange("platon:server:chain_100:blocks",0,9);
        blockSet.forEach(str -> {
            Block block = JSON.parseObject(str,Block.class);
            logger.info("区块编号：{}",block.getNumber());
        });

        logger.info("第2页=============");
        blockSet = redisTemplate.opsForZSet().reverseRange("platon:server:chain_100:blocks",10,19);
        blockSet.forEach(str -> {
            Block block = JSON.parseObject(str,Block.class);
            logger.info("区块编号：{}",block.getNumber());
        });
    }

    @Test
    public void countBlockCache(){
        long count = redisTemplate.opsForZSet().count("platon:server:chain_100:blocks",0,Long.MAX_VALUE);
        logger.info("集合大小：{}", count);
    }

    @Test
    public void removeBlockCacheTail(){
        long count = redisTemplate.opsForZSet().removeRange("platon:server:chain_100:blocks",0,0);
    }

    @Test
    public void existBlock(){
        Set<String> exist = redisTemplate.opsForZSet().rangeByScore("platon:server:chain_100:blocks",462816,462816);
        logger.info("{}",exist);
    }
}

