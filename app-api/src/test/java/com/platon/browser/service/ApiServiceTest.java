package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.req.transaction.TransactionListReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class ApiServiceTest extends ServiceTestBase {
    private static final Logger logger = LoggerFactory.getLogger(ApiServiceTest.class);

    @Autowired
    private ApiService apiService;

    @Test
    public void list(){
        chainsConfig.getChainIds().forEach(chainId -> {
            TransactionListReq tlr = new TransactionListReq();
            tlr.setBeginSequence(153);
            tlr.setListSize(100);
            tlr.setCid("203");
            tlr.setAddress("0xbae514b5f89a90e16535c87bcc72ea0619046a62");

            List<Transaction> result = apiService.transactionList(tlr);


            logger.info("{}",JSON.toJSONString(result,true));
        });
    }

}
