package com.platon.FilterTest;

import com.platon.browser.agent.SpringbootApplication;
import com.platon.browser.agent.client.Web3jClient;
import com.platon.browser.agent.filter.BlockFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.Web3j;

/**
 * User: dongqile
 * Date: 2019/1/10
 * Time: 10:44
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class BlockFilterTest {

    protected static Logger logger = LoggerFactory.getLogger(BlockFilterTest.class);

    @Autowired
    private Web3jClient web3jClient;

    @Test
    public boolean BlockFilter(){
        BlockFilter blockFilter = new BlockFilter();
        Web3j web3j = Web3jClient.getWeb3jClient();
        //blockFilter.build()
        return false;
    }

}