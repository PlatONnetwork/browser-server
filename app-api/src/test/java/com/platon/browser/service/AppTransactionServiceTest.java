package com.platon.browser.service;

import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class AppTransactionServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(AppTransactionServiceTest.class);

    @Test
    public void test(){
        NodeRankingExample example = new NodeRankingExample();
        List<NodeRanking> nodes = nodeRankingMapper.selectByExample(example);
        logger.debug("{}",nodes);
    }

}
