package com.platon.browser.service;

import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodeListReq;
import com.platon.browser.req.node.NodePageReq;
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
import javax.swing.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class NodeServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(NodeServiceTest.class);
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
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private NodeService nodeService;

    @PostConstruct
    private void init(){
        blockCacheKey=blockCacheKeyTemplate.replace("{}",chainId);
        transactionCacheKey=transactionCacheKeyTemplate.replace("{}",chainId);
        nodeCacheKey=nodeCacheKeyTemplate.replace("{}",chainId);
    }

    @Test
    public void insertNode(){
        List<NodeRanking> data = TestDataUtil.generateNode(chainId);
        int actualCount = nodeRankingMapper.batchInsert(data);
        Assert.assertEquals(data.size(),actualCount);
    }

    /*************节点****************/
    @Test
    public void list(){
        NodePageReq req = new NodePageReq();
        req.setCid(chainId);
        req.setKeyword("777");
       /* req.setIsValid(1);
        req.setNodeType(1);*/
        RespPage<NodeListItem> data = nodeService.list(req);
        Assert.assertTrue(data.getData().size()>=0);
    }

    @Test
    public void detail(){
        NodeDetailReq req = new NodeDetailReq();
        req.setCid(chainId);
        req.setId(60l);
        NodeDetail data = nodeService.detail(req);
        Assert.assertNotNull(data);
    }

    @Test
    public void getPushData(){
        List<NodePushItem> data = nodeService.getPushData(chainId);
        Assert.assertTrue(data.size()>=0);
    }


}
