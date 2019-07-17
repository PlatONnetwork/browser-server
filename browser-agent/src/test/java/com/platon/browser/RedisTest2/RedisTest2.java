package com.platon.browser.RedisTest2;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.service.DBService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
public class RedisTest2 extends TestBase {

    protected static Logger logger = LoggerFactory.getLogger(RedisTest2.class);
    private String keyPrefix="browser:0.6.1:test:203:tran-list:";
    private String keyTemplate=keyPrefix+"{address}:{txType}:{txHash}";
    @Test
    public void initBlockCache () {
        String obj = redisTemplate.opsForValue().get("sfsfsdfsf");
        logger.info("{}", obj);
    }

    @Test
    public void test(){
        String cacheKey = transactionCacheKeyTemplate.replace("{}",chainId);
        Set<String> exist = redisTemplate.opsForZSet().reverseRange(cacheKey,0,-1);
        int a = exist.size();
        logger.info("{}",a);
    }

    @Test
    public void batchTransactionListTest(){
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo("203");
        List <TransactionWithBLOBs> list =  transactionMapper.selectByExampleWithBLOBs(transactionExample);
        //dbService.batchInsertTransactionList(list);
    }

    @Test
    public void getBatchTransactionList(){
        String pattern, str;
        pattern =keyTemplate.replace("{address}","0x");
        Collection <TransactionWithBLOBs> res =getResult(pattern);

    }
    private Collection<TransactionWithBLOBs> getResult(String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        Map<String,TransactionWithBLOBs> result = batchQueryByKeys(new ArrayList<>(keys),false,TransactionWithBLOBs.class);
        return result.values();
    }


    public <T> Map<String,T> batchQueryByKeys(List<String> keys,Boolean useParallel,Class<T> clazz){
        if(null == keys || keys.size() == 0 ){
            return null;
        }

        if(null == useParallel){
            useParallel = true;
        }

        List<Object> results = redisTemplate.executePipelined( (RedisCallback <Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
            keys.forEach(key->stringRedisConn.get(key));
            return null;
        });
        if(null == results || results.size() == 0 ){return null;}

        Map<String,T> resultMap  =  null;

        if(useParallel){
            Map<String,T> resultMapOne  = Collections.synchronizedMap(new HashMap<>());
            keys.parallelStream().forEach(key -> {
                String res = (String)results.get(keys.indexOf(key));
                resultMapOne.put(key, JSON.parseObject(res,clazz));
            });
            resultMap = resultMapOne;
        }else{
            Map<String,T> resultMapTwo  = new HashMap<>();
            keys.forEach(key-> {
                String res = (String)results.get(keys.indexOf(key));
                resultMapTwo.put(key,JSON.parseObject(res,clazz));
            });
            resultMap = resultMapTwo;
        }
        return  resultMap;
    }



}

