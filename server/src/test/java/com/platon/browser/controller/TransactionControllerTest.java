package com.platon.browser.controller;

import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.transaction.TransactionListReq;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class TransactionControllerTest extends ControllerTestBase {
    private static final Logger logger = LoggerFactory.getLogger(TransactionControllerTest.class);

    @Test
    public void getPage() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                TransactionListReq req = new TransactionListReq();
                req.setCid(chainId);
                sendPost("/transaction/transactionList",req);

                req.setHeight(10l);
                sendPost("/transaction/transactionList",req);

                req.setPageNo(2);
                req.setPageSize(3);
                sendPost("/transaction/transactionList",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void getDetail() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                TransactionListItem data = getOneTransaction(chainId);
                if(data==null){
                    Assert.fail("No data in the database.");
                    return;
                }

                BlockDetailReq req = new BlockDetailReq();
                req.setCid(chainId);
                req.setHeight(data.getBlockHeight());
                sendPost("/transaction/transactionDetails",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void detailNavigate() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                TransactionListItem data = getOneTransaction(chainId);
                if(data==null){
                    Assert.fail("No data in the database.");
                    return;
                }

                BlockDetailNavigateReq req = new BlockDetailNavigateReq();
                req.setCid(chainId);
                req.setDirection("next");
                req.setHeight(data.getBlockHeight()-10);
                sendPost("/transaction/transactionDetailNavigate",req);

                req.setDirection("prev");
                req.setHeight(data.getBlockHeight());
                sendPost("/transaction/transactionDetailNavigate",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
