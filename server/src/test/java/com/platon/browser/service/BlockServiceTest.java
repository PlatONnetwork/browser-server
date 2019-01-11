package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
public class BlockServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(BlockServiceTest.class);
    @Autowired
    private BlockService blockService;
    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initBlockTable();
            /*BlockPageReq req = new BlockPageReq();
            req.setCid(chainId);
            blockService.getPage(req); */
        });
    }

    @Test
    public void getBlock(){
        chainsConfig.getChainIds().forEach(chainId -> {

        });
    }
}
