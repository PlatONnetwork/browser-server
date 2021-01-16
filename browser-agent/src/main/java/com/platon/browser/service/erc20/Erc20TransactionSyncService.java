package com.platon.browser.service.erc20;

import com.platon.browser.config.RedisKeyConfig;
import com.platon.browser.service.elasticsearch.OldEsErc20TxRepository;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.elasticsearch.dto.OldErcTx;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.redis.OldRedisErc20TxService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class Erc20TransactionSyncService {

    @Resource
    private RedisKeyConfig redisKeyConfig;

    /**
     * 同步ERC20交易
     * 把ES中的ERC20交易同步到Redis中
     */
    @Getter
    @Setter
    private static volatile boolean done =false;
    @Resource
    private OldEsErc20TxRepository oldEsErc20TxRepository;
    @Resource
    private OldRedisErc20TxService redisService;
    @Value("${platon.paging.erc20-transaction.page-size}")
    private int pageSize;
    @Value("${platon.paging.erc20-transaction.page-count}")
    private int pageCount;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void sync(){
        Long recordSize = redisService.size(redisKeyConfig.getErc20Tx());
        // 如果redis innerTx不为空，则不用同步
        if(recordSize>0) return;
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<OldErcTx> esResult=null;
        for (int pageNo = 0; pageNo <= pageCount; pageNo++) {
            try {
                esResult = oldEsErc20TxRepository.search(transactionConstructor, OldErcTx.class,pageNo,pageSize);
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
            List<OldErcTx> transactions = esResult.getRsData();
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
