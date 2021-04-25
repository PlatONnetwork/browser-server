package com.platon.browser.service;

import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.EsTransactionRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.RedisTransactionService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class TransactionSyncService {

    /**
     * 同步交易
     */
    @Getter
    @Setter
    private static volatile boolean done =false;
    @Autowired
    private EsTransactionRepository esRepository;
    @Autowired
    private RedisTransactionService redisService;
    @Value("${paging.transaction.page-size}")
    private int pageSize;
    @Value("${paging.transaction.page-count}")
    private int pageCount;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void sync(){
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<Transaction> esResult=null;
        for (int pageNo = 0; pageNo <= pageCount; pageNo++) {
            try {
                esResult = esRepository.search(transactionConstructor,Transaction.class,pageNo,pageSize);
            } catch (Exception e) {
                if(e.getMessage().contains("all shards failed")){
                    break;
                }else{
                    log.error("【syncTransaction()】查询ES出错:",e);
                }
            }
            if(esResult==null||esResult.getRsData()==null||esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            List<Transaction> transactions = esResult.getRsData();
            try{
                redisService.save(new HashSet<>(transactions),false);
                log.info("【syncTransaction()】第{}页,{}条记录",pageNo,transactions.size());
            }catch (Exception e){
                log.error("【syncTransaction()】同步交易到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(transactions.size()<pageSize) break;
        }
        done=true;
    }
}
