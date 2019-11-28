package com.platon.browser.service;

import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.queue.publisher.BlockEventPublisher;
import com.platon.browser.queue.publisher.TransactionEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SyncService {
    @Autowired
    private BlockEventPublisher blockEventPublisher;

    @Autowired
    private TransactionEventPublisher transactionEventPublisher;

    @Autowired
    private BlockESRepository blockESRepository;

    @Autowired
    private TransactionESRepository transactionESRepository;

    @Value("${esyncnfo.searchBlockPageSize}")
    private int blockPageSize;

    @Value("${esyncnfo.searchTxPageSize}")
    private int txPageSize;

    public void syncBlock(){
        ESQueryBuilderConstructor blockConstructor = new ESQueryBuilderConstructor();
        blockConstructor.setDesc("num");
        // 分页查询区块数据
        ESResult<Block> esResult=null;
        for (int pageNo = 0; pageNo <= 1000; pageNo++) {
            try {
                esResult = blockESRepository.search(blockConstructor, Block.class, pageNo, blockPageSize);
            } catch (IOException e) {
                log.error("查询ES出错:",e);
            }
            if(esResult!=null&&esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            try{
                blockEventPublisher.publish(esResult.getRsData());
            }catch (Exception e){
                log.error("发布区块到队列出错:",e);
            }
        }
    }

    public void syncTransaction(){
        ESQueryBuilderConstructor transactionConstructor = new ESQueryBuilderConstructor();
        transactionConstructor.setDesc("seq");
        // 分页查询区块数据
        ESResult<Transaction> esResult=null;
        for (int pageNo = 0; pageNo <= 100; pageNo++) {
            try {
                esResult = transactionESRepository.search(transactionConstructor,Transaction.class,pageNo,txPageSize);
            } catch (IOException e) {
                log.error("查询ES出错:",e);
            }
            if(esResult!=null&&esResult.getTotal()==0){
                // 如果查询结果为空则结束
                break;
            }
            try{
                transactionEventPublisher.publish(esResult.getRsData());
            }catch (Exception e){
                log.error("发布区块到队列出错:",e);
            }
        }
    }
}
