package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class RedisCacheServiceTransactionTest extends RedisCacheServiceBaseTest{
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceTransactionTest.class);

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
