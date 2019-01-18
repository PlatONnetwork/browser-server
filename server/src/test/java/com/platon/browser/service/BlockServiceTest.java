package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockDownloadReq;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.util.DataTool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class BlockServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);

    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTableAndCache();
            List<Block> originData = DataTool.getTestData(chainId,TestDataFileNameEnum.BLOCK, Block.class);
            BlockPageReq req = new BlockPageReq();
            req.setCid(chainId);
            req.setPageSize(originData.size());
            RespPage<BlockListItem> result = blockService.getPage(req);
            Assert.assertEquals(originData.size(),result.getData().size());
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTableAndCache();
            BlockListItem data = getOneBlock(chainId);
            BlockDetailReq req = new BlockDetailReq();
            req.setCid(chainId);
            req.setHeight(data.getHeight());
            BlockDetail result = blockService.getDetail(req);
            Assert.assertEquals(data.getHash(),result.getHash());
        });
    }

    @Test
    public void getList(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTableAndCache();
            initNodeRankingTableAndCache();
            BlockListItem data = getOneBlock(chainId);
            BlockDownloadReq req = new BlockDownloadReq();
            req.setCid(chainId);
            req.setNodeId(data.getNodeId());
            List<Block> result = blockService.getList(req);
            Assert.assertTrue(result.size()>=0);
        });
    }
}
