package com.platon.browser.now.service;

import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.TokenTransferRecordESRepository;
import com.platon.browser.elasticsearch.dto.ESTokenTransferRecord;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.RedisTransferTokenRecordService;
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
public class Erc20TransactionSyncService {

    /**
     * 同步ERC20交易
     * 把ES中的ERC20交易同步到Redis中
     */
    @Getter
    @Setter
    private static volatile boolean done =false;
    @Autowired
    private TokenTransferRecordESRepository esRepository;
    @Autowired
    private RedisTransferTokenRecordService redisService;
    @Value("${platon.paging.erc20-transaction.page-size}")
    private int pageSize;
    @Value("${platon.paging.erc20-transaction.page-count}")
    private int pageCount;
    /**
     * 交易缓存key
     */
    @Value("${spring.redis.key.innerTx}")
    private String innerTxCacheKey;
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void sync(){
        Long recordSize = redisService.size(innerTxCacheKey);
        // 如果redis innerTx不为空，则不用同步
        if(recordSize>0) return;
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<ESTokenTransferRecord> esResult=null;
        for (int pageNo = 0; pageNo <= pageCount; pageNo++) {
            try {
                esResult = esRepository.search(transactionConstructor,ESTokenTransferRecord.class,pageNo,pageSize);
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
            List<ESTokenTransferRecord> transactions = esResult.getRsData();
            try{
                redisService.save(new HashSet<>(transactions),false);
                log.info("【syncEsErc20Transaction2Redis()】第{}页,{}条记录",pageNo,transactions.size());
            }catch (Exception e){
                log.error("【syncEsErc20Transaction2Redis()】同步交易到Redis出错:",e);
                throw e;
            }
            // 所有数据不够一页大小，退出
            if(transactions.size()<pageSize) break;
        }
        done=true;
    }
}