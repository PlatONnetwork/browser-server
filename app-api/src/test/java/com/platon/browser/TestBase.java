package com.platon.browser;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodeListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.node.NodePageReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.service.*;
import com.platon.browser.service.app.AppNodeService;
import com.platon.browser.service.app.AppTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@SpringBootTest(classes= AppApiApplication.class, value = "spring.profiles.active=unittest")
public class TestBase extends TestData {

    @Autowired
    protected ChainsConfig chainsConfig;
    @Autowired
    protected AppNodeService appNodeService;
    @Autowired
    protected AppTransactionService appTransactionService;

    @Autowired
    protected RedisTemplate<String,String> redisTemplate;

    @Autowired
    protected PlatonClient platon;

}
