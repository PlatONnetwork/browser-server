package com.platon.browser.redis;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.ServiceApplication;
import com.platon.browser.config.ChainsConfig;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServiceApplication.class)
public class RedisCacheInit {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private ChainsConfig chainsConfig;

    protected static Logger logger = LoggerFactory.getLogger(RedisCacheInit.class);

    @Test
    public void initBlockCache() {
        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_"+chainId+":blocks";;

            for(int i=0;i<500;i++){
                BlockExample condition = new BlockExample();
                condition.createCriteria().andChainIdEqualTo("1");
                condition.setOrderByClause("number desc");

                PageHelper.startPage(i+1,1000);
                List<Block> blockList = blockMapper.selectByExample(condition);
                if(blockList.size()==0) break;

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
            }

            long size = redisTemplate.opsForZSet().size(cacheKey);
            if(size>500000){
                // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000);
            }
        });
    }

    @Test
    public void catchUpBlockCache() {
        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_"+chainId+":blocks";
            while(true){
                BlockExample condition = new BlockExample();
                condition.createCriteria().andChainIdEqualTo(chainId);
                condition.setOrderByClause("number desc");

                PageHelper.startPage(1,2000);
                List<Block> blockList = blockMapper.selectByExample(condition);
                if(blockList.size()==0) break;

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

                long size = redisTemplate.opsForZSet().size(cacheKey);
                if(size>500000){
                    // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                    redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000-1);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void initTransactionCache() {
        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_" + chainId + ":transactions";
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId);
            condition.setOrderByClause("block_number desc,transaction_index desc");
            for(int i=0;i<500;i++){
                PageHelper.startPage(i+1,1000);
                List<Transaction> transactionList = transactionMapper.selectByExample(condition);
                if(transactionList.size()==0) break;

                Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
                transactionList.forEach(transaction -> {
                    Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,transaction.getSequence(),transaction.getSequence());
                    if(exist.size()==0){
                        // 在缓存中不存在的才放入缓存
                        tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(transaction),transaction.getSequence().doubleValue()));
                    }
                });
                if(tupleSet.size()>0){
                    redisTemplate.opsForZSet().add(cacheKey, tupleSet);
                }
            }
            long size = redisTemplate.opsForZSet().size(cacheKey);
            if(size>500000){
                // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                long count = redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000);
            }
        });

    }

    @Test
    public void catchUpTransactionCache() {
        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_" + chainId + ":transactions";
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId);
            condition.setOrderByClause("block_number desc,transaction_index desc");
            while(true){
                PageHelper.startPage(1,1000);
                List<Transaction> transactionList = transactionMapper.selectByExample(condition);
                if(transactionList.size()==0) break;

                Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
                transactionList.forEach(transaction -> {
                    Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,transaction.getSequence(),transaction.getSequence());
                    if(exist.size()==0){
                        // 在缓存中不存在的才放入缓存
                        tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(transaction),transaction.getSequence().doubleValue()));
                    }
                });
                if(tupleSet.size()>0){
                    redisTemplate.opsForZSet().add(cacheKey, tupleSet);
                }

                long size = redisTemplate.opsForZSet().size(cacheKey);
                if(size>500000){
                    // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                    redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000-1);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

