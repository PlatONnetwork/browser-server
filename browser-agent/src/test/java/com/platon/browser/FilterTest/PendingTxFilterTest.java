package com.platon.browser.FilterTest;

import com.platon.browser.TestBase;
import com.platon.browser.client.PlatonClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.List;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:40
 */
@RunWith(SpringRunner.class)
public class PendingTxFilterTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(PendingTxFilterTest.class);

    @Autowired
    private PlatonClient platon;

    @Value("${platon.chain.active}")
    private String chainId;

    @Test
    public void PengdingTxFilterTest () {
        try {
            logger.info("PengdingTxFilterTest begin");
            List <Transaction> list = DataBuild.DataBuild(Transaction.class);
            pendingFilter.analyse(list, chainId, platon);
            logger.info("PengdingTxFilterTest end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}