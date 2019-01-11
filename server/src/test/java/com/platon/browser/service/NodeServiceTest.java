package com.platon.browser.service;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class NodeServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(NodeServiceTest.class);
    @Autowired
    private NodeService nodeService;

    /*************节点****************/
    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodePageReq req = new NodePageReq();
            req.setCid(chainId);
            req.setKeyword("777");
           /* req.setIsValid(1);
            req.setNodeType(1);*/
            RespPage<NodeListItem> data = nodeService.getPage(req);
            Assert.assertTrue(data.getData().size()>=0);
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodeDetailReq req = new NodeDetailReq();
            req.setCid(chainId);
            req.setId(60l);
            NodeDetail data = nodeService.getDetail(req);
            Assert.assertNotNull(data);
        });
    }

    @Test
    public void getPushData(){
        chainsConfig.getChainIds().forEach(chainId -> {
            List<NodePushItem> data = nodeService.getPushData(chainId);
            Assert.assertTrue(data.size()>=0);
        });
    }
}
