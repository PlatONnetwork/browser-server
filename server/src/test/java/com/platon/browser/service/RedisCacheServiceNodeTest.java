package com.platon.browser.service;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
public class RedisCacheServiceNodeTest extends RedisCacheServiceBaseTest{
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceNodeTest.class);

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
        List<NodeInfo> nodeInfoList = redisCacheService.getNodeList(chainId);
        Assert.assertEquals(nodes.size(),nodeInfoList.size());
    }
}
