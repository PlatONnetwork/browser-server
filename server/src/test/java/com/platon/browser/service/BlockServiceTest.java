package com.platon.browser.service;

import com.platon.browser.req.block.BlockPageReq;
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
            blockService.getPage(req);
        });
    }

    @Test
    public void getBlock(){
        chainsConfig.getChainIds().forEach(chainId -> {

        });
    }
}
