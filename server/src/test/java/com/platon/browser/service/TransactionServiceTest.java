package com.platon.browser.service;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.util.DataTool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class TransactionServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceTest.class);

    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTableAndCache();
            List<TransactionWithBLOBs> originData = DataTool.getTestData(chainId,TestDataFileNameEnum.TRANSACTION, TransactionWithBLOBs.class);
            TransactionPageReq req = new TransactionPageReq();
            req.setCid(chainId);
            req.setPageSize(originData.size());
            RespPage<TransactionListItem> result = transactionService.getPage(req);
            Assert.assertEquals(originData.size(),result.getData().size());
        });
    }

    @Test
    public void getDetail(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTableAndCache();
            TransactionListItem data = getOneTransaction(chainId);
            TransactionDetailReq req = new TransactionDetailReq();
            req.setCid(chainId);
            req.setTxHash(data.getTxHash());
            TransactionDetail result = transactionService.getDetail(req);
            Assert.assertEquals(data.getTxHash(),result.getTxHash());
        });
    }
}
