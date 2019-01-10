package com.platon.browser.service;

import com.platon.browser.ServerApplication;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.search.SearchResult;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.search.SearchReq;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ServerApplication.class, value = "spring.profiles.active=1")
public class SearchServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceTest.class);
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
    private SearchService searchService;

    @PostConstruct
    private void init(){
        blockCacheKey=blockCacheKeyTemplate.replace("{}",chainId);
        transactionCacheKey=transactionCacheKeyTemplate.replace("{}",chainId);
        nodeCacheKey=nodeCacheKeyTemplate.replace("{}",chainId);
    }

    /*************节点****************/
    @Test
    public void searchNode(){
        SearchReq req = new SearchReq();
        req.setCid(chainId);
        req.setParameter("0x1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429");
        SearchResult data = searchService.search(req);
        Assert.assertEquals("node",data.getType());

        req.setParameter("d7775");
        data = searchService.search(req);
        Assert.assertEquals("node",data.getType());

        req.setParameter("27");
        data = searchService.search(req);
        Assert.assertEquals("block",data.getType());

        req.setParameter("0x64f34a510b8aba47af32383499c6f364c963ab9436865dc32aad9ab74a1cab5f");
        data = searchService.search(req);
        Assert.assertTrue("block".equals(data.getType())||"transaction".equals(data.getType()));

        req.setParameter("0x6ce6f581afc92989fb9e367a66c4a4b5557a9a1e7fe1978fcf020d32254d589d");
        data = searchService.search(req);
        Assert.assertTrue("block".equals(data.getType())||"transaction".equals(data.getType()));
    }
}
