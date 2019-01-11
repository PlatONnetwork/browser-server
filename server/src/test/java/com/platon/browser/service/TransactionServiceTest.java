package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);

    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTable();
            TransactionPageReq req = new TransactionPageReq();
            req.setCid(chainId);
            RespPage<TransactionListItem> result = transactionService.getPage(req);
            Assert.assertTrue(result.getData().size()>=0);
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTable();
            TransactionListItem data = getOneTransaction(chainId);
            TransactionDetailReq req = new TransactionDetailReq();
            req.setCid(chainId);
            req.setTxHash(data.getTxHash());
            TransactionDetail result = transactionService.getDetail(req);
            Assert.assertEquals(data.getTxHash(),result.getTxHash());
        });
    }
}
