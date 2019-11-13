package com.platon.browser.bootstrap.service;


import com.platon.browser.bootstrap.queue.callback.ShutdownCallback;
import com.platon.browser.bootstrap.queue.publisher.BootstrapEventPublisher;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.collection.service.block.BlockService;
import com.platon.browser.collection.service.transaction.ReceiptService;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.mapper.NetworkStatMapper;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.util.SleepUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @description: MySQL/ES/Redis启动一致性自检服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class ConsistencyService {

    @Autowired
    private NetworkStatMapper networkStatMapper;

    @Autowired
    private BlockESRepository blockESRepository;
    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private BlockService blockService;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private BootstrapEventPublisher bootstrapEventPublisher;

    /**
     * 开机自检，检查es、redis中的区块高度和交易序号是否和mysql数据库一致，以mysql的数据为准
     * @throws IOException
     */
    public void post() throws IOException {
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if(networkStat==null) {
            return;
        }

        // mysql中的最高块号
        long mysqlMaxBlockNum = networkStat.getCurNumber();
        log.warn("MYSQL最高区块号:{}",mysqlMaxBlockNum);
        // mysql中的最高交易序号ID
        long mysqlTransactionId = networkStat.getTxQty();
        log.warn("MYSQL最高交易序号:{}",mysqlTransactionId);
        // 计算同步开始区块号
        long esMaxBlockNum = mysqlMaxBlockNum;
        boolean exist =false;
        while (!exist){
            exist = blockESRepository.exists(String.valueOf(esMaxBlockNum));
            if(!exist) {
                esMaxBlockNum--;
                // 小于等于0时需要退出
                if(esMaxBlockNum<=0) {
                    exist=true;
                }
            }
        }
        log.warn("ES区块索引最高区块号:{}",esMaxBlockNum);
        // 取ES中的交易最高序号
        long esMaxTransactionId = mysqlTransactionId;
        exist=false;
        while (!exist){
            Transaction tx = transactionESRepository.get(String.valueOf(esMaxTransactionId), Transaction.class);
            if(tx==null){
                esMaxTransactionId--;
                // 小于等于0时需要退出
                if(esMaxTransactionId<=0) exist=true;
            } else {
                exist=true;
                // 更新区块号，以最小块号为准
                esMaxBlockNum=tx.getNum()<esMaxBlockNum?tx.getNum():esMaxBlockNum;
                log.warn("ES交易索引最高交易序号:{}",tx.getId());
            }
        }
        log.warn("ES交易索引最高区块号:{}",esMaxBlockNum);

        if(esMaxBlockNum>=mysqlMaxBlockNum) {
            log.warn("MYSQL/ES/REDIS中的数据已同步!");
            return;
        }

        long startBlockNum = esMaxBlockNum+1;
        log.warn("MYSQL/ES/REDIS数据同步区间:[{},{}]",startBlockNum,mysqlMaxBlockNum);
        // 补充 [startBlockNum,mysqlMaxBlockNum] 闭区间内的区块和交易信息到ES和Redis
        ShutdownCallback callback = ShutdownCallback.builder()
                .endBlockNum(mysqlMaxBlockNum)
                .build();
        for (long number=startBlockNum;number<=mysqlMaxBlockNum;number++){
            // 异步获取区块
            CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(number);
            // 异步获取交易回执
            CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(number);
            bootstrapEventPublisher.publish(blockCF,receiptCF,callback);
        }
        while (!callback.isDone()) SleepUtil.sleep(1L);
        bootstrapEventPublisher.shutdown();
        log.warn("MYSQL/ES/REDIS中的数据同步完成!");
    }
}
