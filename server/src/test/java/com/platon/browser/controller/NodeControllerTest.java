package com.platon.browser.controller;

import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockListReq;
import com.platon.browser.req.node.NodeDetailReq;
import com.platon.browser.req.node.NodePageReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class NodeControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(NodeControllerTest.class);

    @Test
    public void getPage() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                NodePageReq req = new NodePageReq();
                req.setCid(chainId);
                sendPost("/node/list",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void getHistory() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                NodePageReq req = new NodePageReq();
                req.setCid(chainId);
                sendPost("/node/history",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void detail() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                NodeListItem node = getOneNode(chainId);
                if(node==null){
                    Assert.fail("No data in the database.");
                    return;
                }
                NodeDetailReq req = new NodeDetailReq();
                req.setCid(chainId);
                req.setId(node.getId());
                sendPost("/node/detail",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void blockList() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                NodeListItem node = getOneNode(chainId);
                if(node==null){
                    Assert.fail("No data in the database.");
                    return;
                }
                BlockListReq req = new BlockListReq();
                req.setCid(chainId);
                req.setAddress(node.getAddress());
                sendPost("/node/blockList",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void blockDownload() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                NodeListItem node = getOneNode(chainId);
                if(node==null){
                    Assert.fail("No data in the database.");
                    return;
                }

                BlockDownloadReq req = new BlockDownloadReq();
                req.setCid(chainId);
                req.setAddress(node.getAddress());

                mockMvc.perform(get("/node/blockDownload")
                        .param("cid",chainId)
                        .param("address",node.getAddress())
                        .param("date","2018-01-01")
                )
                        .andExpect(status().isOk())
                        .andDo(print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
