package com.platon.browser.service;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AddressDetailReq;
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
public class PendingTxServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(PendingTxServiceTest.class);

    @Test
    public void getPage(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTableAndCache();
            List<TransactionWithBLOBs> originData = DataTool.getTestData(chainId, TestDataFileNameEnum.TRANSACTION, TransactionWithBLOBs.class);
            TransactionPageReq req = new TransactionPageReq();
            req.setCid(chainId);
            req.setPageSize(originData.size());
            RespPage<TransactionListItem> result = transactionService.getPage(req);
            Assert.assertEquals(originData.size(),result.getData().size());
        });
    }

    @Test
    public void getList(){
        chainsConfig.getChainIds().forEach(chainId -> {
            initTransactionTableAndCache();
            TransactionListItem transaction = getOneTransaction(chainId);
            AddressDetailReq req = new AddressDetailReq();
            req.setCid(chainId);
            req.setAddress(transaction.getFrom());
            StringBuilder sb = new StringBuilder();
            sb.append(TransactionTypeEnum.getEnum(transaction.getTxType()).code).append(",")
                    .append(TransactionTypeEnum.TRANSACTION_CANDIDATE_DEPOSIT.code).append(",")
                    .append(TransactionTypeEnum.TRANSACTION_CANDIDATE_WITHDRAW.code).append(",")
                    .append(TransactionTypeEnum.TRANSACTION_CANDIDATE_APPLY_WITHDRAW.code);
            //req.setTxType(sb.toString());
            List<TransactionWithBLOBs> result = transactionService.getList(req);
            Assert.assertTrue(result.size()>0);
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
