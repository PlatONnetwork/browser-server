package com.platon.browser.controller;

import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockPageReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BlockControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(BlockControllerTest.class);

    @Test
    public void blockList() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                BlockPageReq req = new BlockPageReq();
                req.setCid(chainId);
                sendPost("/block/blockList",req);
                req.setPageNo(2);

                req.setPageSize(5);
                sendPost("/block/blockList",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void blockDetail() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                BlockListItem block = getOneBlock(chainId);
                BlockDetailReq req = new BlockDetailReq();
                req.setCid(chainId);
                req.setHeight(block.getHeight());
                sendPost("/block/blockDetails",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void blockDetailNavigate() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                BlockListItem block = getOneBlock(chainId);
                BlockDetailNavigateReq req = new BlockDetailNavigateReq();
                req.setCid(chainId);
                req.setDirection("next");
                req.setHeight(block.getHeight()-10);
                sendPost("/block/blockDetailNavigate",req);

                req.setDirection("prev");
                req.setHeight(block.getHeight());
                sendPost("/block/blockDetailNavigate",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
