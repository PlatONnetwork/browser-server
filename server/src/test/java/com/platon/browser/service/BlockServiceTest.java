package com.platon.browser.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockPageReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class BlockServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);

    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTable();
            BlockPageReq req = new BlockPageReq();
            req.setCid(chainId);
            RespPage<BlockListItem> blocks = blockService.getPage(req);
            Assert.assertTrue(blocks.getData().size()>=0);
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTable();
            BlockListItem data = getOneBlock(chainId);
            BlockDetailReq req = new BlockDetailReq();
            req.setCid(chainId);
            req.setHeight(data.getHeight());
            BlockDetail result = blockService.getDetail(req);
            Assert.assertEquals(data.getHash(),result.getHash());
        });
    }
}
