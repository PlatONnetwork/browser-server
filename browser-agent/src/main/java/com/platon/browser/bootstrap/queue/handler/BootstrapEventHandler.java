package com.platon.browser.bootstrap.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.client.result.ReceiptResult;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.dao.entity.Delegation;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class BootstrapEventHandler implements EventHandler<BootstrapEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;

    private Set<Block> blocks=new HashSet<>();
    private Set<Transaction> transactions=new HashSet<>();
    private Set<Delegation> delegations=new HashSet<>();
    private Set<NodeOpt> nodeOpts=new HashSet<>();
    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BootstrapEventHandler")
    public void onEvent(BootstrapEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException {
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            ReceiptResult receiptResult = event.getReceiptCF().get();
            CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(rawBlock,receiptResult);

            clear();
            blocks.add(block);
            transactions.addAll(block.getTransactions());
            block.setTransactions(null);
            esImportService.batchImport(blocks,transactions,delegations,nodeOpts);
            redisImportService.batchImport(blocks,transactions, Collections.emptySet());
            clear();
            event.getCallback().call(block.getNum());
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }

    private void clear(){
        blocks.clear();
        transactions.clear();
        delegations.clear();
        nodeOpts.clear();
    }
}