package com.platon.browser.service.cache;

import com.alibaba.fastjson.JSON;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.ticket.Ticket;
import com.platon.browser.enums.TransactionTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

@RunWith(SpringRunner.class)
public class TransactionCacheServiceTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(TransactionCacheServiceTest.class);

    @Autowired
    private TransactionCacheService transactionCacheService;

    @Test
    public void insertData () throws FileNotFoundException {


        BufferedReader br = new BufferedReader(new FileReader("D:\\Workspace\\browser-server\\browser-api\\src\\test\\resources\\transactions.json"));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(line->sb.append(line));
        List<TransactionWithBLOBs> transactions = JSON.parseArray(sb.toString(), TransactionWithBLOBs.class);

        transactionCacheService.classifyByAddress("100",transactions);

        logger.error("insert done");
    }

    @Test
    public void queryData () throws FileNotFoundException {

        Collection<TransactionWithBLOBs> transactions;

        // 查所有交易
        //transactions = transactionCacheService.fuzzyQuery("100",null,null,null,null);
        // 查投票交易
        transactions = transactionCacheService.fuzzyQuery("100",null, TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,null,null);
        // 查转账交易
        transactions = transactionCacheService.fuzzyQuery("100",null, TransactionTypeEnum.TRANSACTION_TRANSFER.code,null,null);
        // 查创建合约交易
        transactions = transactionCacheService.fuzzyQuery("100",null, TransactionTypeEnum.TRANSACTION_CONTRACT_CREATE.code,null,null);
        logger.error("query done");
    }
}

