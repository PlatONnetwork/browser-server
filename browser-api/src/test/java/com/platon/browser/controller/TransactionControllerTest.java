package com.platon.browser.controller;

import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.transaction.TransactionPageReq;
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
                TransactionPageReq req = new TransactionPageReq();
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
                // 同步缓存
                sendGet("/cache/reset/"+chainId+"/transaction");

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
    public void addressDetails() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                // 同步缓存
                sendGet("/cache/reset/"+chainId+"/transaction");

                TransactionListItem data = getOneTransaction(chainId);
                if(data==null){
                    Assert.fail("No data in the database.");
                    return;
                }

                // 一般查询
                AddressDetailReq req = new AddressDetailReq();
                req.setCid(chainId);
                req.setAddress(data.getFrom());
                sendPost("/transaction/addressDetails",req);

                // 投票交易查询
                req.setTxType(TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code);
                sendPost("/transaction/addressDetails",req);

                // 质押交易查询
                StringBuilder sb = new StringBuilder();
                sb.append(TransactionTypeEnum.TRANSACTION_CANDIDATE_DEPOSIT.code).append(",")
                        .append(TransactionTypeEnum.TRANSACTION_CANDIDATE_WITHDRAW.code).append(",")
                        .append(TransactionTypeEnum.TRANSACTION_CANDIDATE_APPLY_WITHDRAW.code);
                req.setTxType(sb.toString());
                sendPost("/transaction/addressDetails",req);

                // 指定交易类型查询
                req.setTxType(data.getTxType());
                sendPost("/transaction/addressDetails",req);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void detailNavigate() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                // 同步缓存
                sendGet("/cache/reset/"+chainId+"/transaction");

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
