package com.platon.browser.service;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.TxInfo;
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

    @Test
    public void patchTransactionData(){
        TransactionExample con = new TransactionExample();
        List<TransactionWithBLOBs> list =transactionMapper.selectByExampleWithBLOBs(con);
        list.forEach(transaction -> {
            if("voteTicket".equals(transaction.getTxType())){
                TxInfo txInfo = JSON.parseObject(transaction.getTxInfo(), TxInfo.class);
                transaction.setCol1(txInfo.getFunctionName());
                transaction.setCol2(txInfo.getParameters().getPrice().toString());
                transaction.setCol3(txInfo.getParameters().getCount().toString());
                transactionMapper.updateByPrimaryKeyWithBLOBs(transaction);
            }
        });

    }

}
