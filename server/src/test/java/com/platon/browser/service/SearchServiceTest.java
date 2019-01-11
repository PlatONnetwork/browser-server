package com.platon.browser.service;

import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.search.SearchResult;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.search.SearchReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SearchServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceTest.class);
    @Autowired
    private SearchService searchService;

    /*************节点****************/
    @Test
    public void searchNode(){
        // 搜索节点
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTable();

            SearchReq req = new SearchReq();
            req.setCid(chainId);
            NodeListItem node = getOneNode(chainId);

            req.setParameter(node.getNodeId());
            SearchResult data = searchService.search(req);
            Assert.assertEquals("node",data.getType());
        });
    }

    @Test
    public void searchBlock(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTable();

            SearchReq req = new SearchReq();
            req.setCid(chainId);

            BlockListItem block = getOneBlock(chainId);
            req.setParameter(String.valueOf(block.getHeight()));
            SearchResult data = searchService.search(req);
            Assert.assertTrue("block".equals(data.getType()));

            req.setParameter(block.getHash());
            data = searchService.search(req);
            Assert.assertTrue("block".equals(data.getType()));
        });
    }

    @Test
    public void searchTransaction(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTable();

            SearchReq req = new SearchReq();
            req.setCid(chainId);

            TransactionListItem transaction = getOneTransaction(chainId);
            req.setParameter(transaction.getTxHash());
            SearchResult data = searchService.search(req);
            Assert.assertTrue("block".equals(data.getType())||"transaction".equals(data.getType()));
        });
    }

}
