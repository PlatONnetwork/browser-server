package com.platon.browser.persistence.service;

import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: Chendongming
 * @Date: 2019/10/25 15:12
 * @Description: ES服务
 */
@Slf4j
@Component
public class EsService {
    private static Logger logger = LoggerFactory.getLogger(EsService.class);

    @Autowired
    private BlockESRepository blockESRepository;
    @Autowired
    private TransactionESRepository transactionESRepository;
    @Autowired
    private NodeOptESRepository nodeOptESRepository;

    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void batchInsertOrUpdate (List<Block> blocks, List<Transaction> transactions, List<NodeOpt> nodeOpts) throws IOException {
        if(!blocks.isEmpty()){
            Map<String,Block> blockMap = new HashMap<>();
            blocks.forEach(b->blockMap.put(String.valueOf(b.getNum()),b));
            blockESRepository.bulkAddOrUpdate(blockMap);
        }

        if(!transactions.isEmpty()){
            Map<String,Transaction> transactionMap = new HashMap<>();
            transactions.forEach(t->transactionMap.put(String.valueOf(t.getHash()),t));
            transactionESRepository.bulkAddOrUpdate(transactionMap);
        }
    }
}
