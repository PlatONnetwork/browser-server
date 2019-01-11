package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
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
            initNodeRankingTable();
            NodePageReq req = new NodePageReq();
            req.setCid(chainId);
            req.setKeyword("777");
            req.setIsValid(1);
            req.setNodeType(1);
            RespPage<NodeListItem> data = nodeService.getPage(req);
            Assert.assertTrue(data.getData().size()>=0);
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initNodeRankingTable();
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
            initNodeRankingTable();
            List<NodePushItem> data = nodeService.getPushCache(chainId);
            Assert.assertTrue(data.size()>=0);
        });
    }
}
