package com.platon.browser.service;

import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.util.DataTool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class NodeServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(NodeServiceTest.class);

    /*************节点****************/
    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTableAndCache();
            List<NodeRanking> originData = DataTool.getTestData(chainId, TestDataFileNameEnum.NODE, NodeRanking.class);
            NodePageReq req = new NodePageReq();
            req.setCid(chainId);
            req.setPageSize(originData.size());
            RespPage<NodeListItem> result = nodeService.getPage(req);
            Assert.assertEquals(originData.size(),result.getData().size());
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTableAndCache();
            NodeListItem data = getOneNode(chainId);
            NodeDetailReq req = new NodeDetailReq();
            req.setCid(chainId);
            req.setId(data.getId());
            NodeDetail result = nodeService.getDetail(req);
            Assert.assertEquals(data.getId(),result.getId());
        });
    }

    @Test
    public void getPushData(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTableAndCache();
            List<NodePushItem> result = nodeService.getPushCache(chainId);
            Assert.assertTrue(result.size()>=0);
        });
    }

    @Test
    public void nodes(){
        PageHelper.startPage(1000000,1000000000);
        List<Transaction> blocks = transactionMapper.selectByExample(new TransactionExample());
        System.out.println(blocks.size());
    }
}
