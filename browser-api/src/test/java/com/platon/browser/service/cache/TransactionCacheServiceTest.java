package com.platon.browser.service.cache;

import com.github.pagehelper.PageHelper;
import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.util.RedisPipelineTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.util.*;

@RunWith(SpringRunner.class)
public class TransactionCacheServiceTest extends TestBase {

    private static Logger logger = LoggerFactory.getLogger(TransactionCacheServiceTest.class);

    @Autowired
    private TransactionCacheService transactionCacheService;

    @Test
    public void insertData () throws FileNotFoundException {

        /*BufferedReader br = new BufferedReader(new FileReader("D:\\Workspace\\browser-server\\browser-api\\src\\test\\resources\\transactions.json"));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(line->sb.append(line));
        List<TransactionWithBLOBs> transactions = JSON.parseArray(sb.toString(), TransactionWithBLOBs.class);
        transactionCacheService.classifyByAddress("500",transactions);*/

        int page = 1;
        while (true){
            try{
                PageHelper.startPage(page,5000);
                page++;
                List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(new TransactionExample());
                if(transactions.size()==0) break;
                String chainId = transactions.get(0).getChainId();
                transactionCacheService.classifyByAddress(chainId,transactions);
            }catch (Exception e){
                logger.error(e.getMessage());
                logger.error("Page num: {}",page);
            }
        }
        logger.error("insert done");
    }

    @Test
    public void queryData () throws FileNotFoundException {

        Collection<TransactionWithBLOBs> transactions;

        // 查所有交易
        //transactions = transactionCacheService.fuzzyQuery("100",null,null,null,null);
        // 查投票交易
        transactions = transactionCacheService.fuzzyQuery("500",null, TransactionTypeEnum.TRANSACTION_VOTE_TICKET.code,null,null);
        // 查转账交易
        transactions = transactionCacheService.fuzzyQuery("500",null, TransactionTypeEnum.TRANSACTION_TRANSFER.code,null,null);
        // 查创建合约交易
        transactions = transactionCacheService.fuzzyQuery("500",null, TransactionTypeEnum.TRANSACTION_CONTRACT_CREATE.code,null,null);
        logger.error("query done");
    }


    @Test
    public void batchDelete () {
        Set<String> keys = redisTemplate.keys("browser:0.6.1:online:chain203:address-trans-list*");

        long count = RedisPipelineTool.batchDeleteByKeys(new ArrayList<>(keys),false,redisTemplate);
        logger.error("Deleted: {}",count);
    }
}

