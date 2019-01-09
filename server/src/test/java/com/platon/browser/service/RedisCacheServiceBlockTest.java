package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public class RedisCacheServiceBlockTest extends RedisCacheServiceBaseTest{
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceBlockTest.class);

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
        RespPage<BlockItem> cache = redisCacheService.getBlockPage(chainId,1,data.size());
        Assert.assertEquals(data.size(),cache.getData().size());
    }


}
