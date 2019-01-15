package com.platon.FilterTest;

import com.platon.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;

/**
 * User: dongqile
 * Date: 2019/1/11
 * Time: 15:40
 */
@RunWith(SpringRunner.class)
public class PendingTxFilterTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(PendingTxFilterTest.class);

    @Test
    public void PengdingTxFilterTest(){
        try {
            Web3j web3j = web3jClient.getWeb3jClient();
            EthPendingTransactions ethPendingTransactions = web3j.ethPendingTx().send();
            pendingFilter.pendingTxAnalysis(ethPendingTransactions);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}