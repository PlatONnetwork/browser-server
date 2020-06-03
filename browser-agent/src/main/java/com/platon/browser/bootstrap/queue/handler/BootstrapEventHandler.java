package com.platon.browser.bootstrap.queue.handler;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.bootstrap.queue.event.BootstrapEvent;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.ReceiptResult;
import com.platon.browser.client.SpecialApi;
import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.AddressCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.service.elasticsearch.EsImportService;
import com.platon.browser.common.service.redis.RedisImportService;
import com.platon.browser.common.utils.BakDataDeleteUtil;
import com.platon.browser.dao.entity.NOptBak;
import com.platon.browser.dao.entity.NOptBakExample;
import com.platon.browser.dao.entity.TxBak;
import com.platon.browser.dao.entity.TxBakExample;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BeanCreateOrUpdateException;
import com.platon.browser.exception.BlankResponseException;
import com.platon.browser.exception.ContractInvokeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.PlatonBlock;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 自检事件处理器
 */
@Slf4j
@Component
public class BootstrapEventHandler implements EventHandler<BootstrapEvent> {

    @Autowired
    private EsImportService esImportService;
    @Autowired
    private RedisImportService redisImportService;
    @Autowired
    private TxBakMapper txBakMapper;
    @Autowired
    private NOptBakMapper nOptBakMapper;
    @Autowired
    private PlatOnClient platOnClient;
    @Autowired
    private AddressCache addressCache;
    @Autowired
    private SpecialApi specialApi;

    private Set<Block> blocks=new HashSet<>();
    private Set<Transaction> transactions=new HashSet<>();

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE,label = "BootstrapEventHandler")
    public void onEvent(BootstrapEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException, ContractInvokeException, BlankResponseException {
        long startTime = System.currentTimeMillis();

        log.debug("BootstrapEvent处理:{}(event(blockCF({}),transactions({})),sequence({}),endOfBatch({}))",
                Thread.currentThread().getStackTrace()[1].getMethodName(),event.getBlockCF().toString(),event.getReceiptCF().toString(),sequence,endOfBatch);
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            ReceiptResult receiptResult = event.getReceiptCF().get();
            CollectionBlock block = CollectionBlock.newInstance().updateWithRawBlockAndReceiptResult(rawBlock,receiptResult,platOnClient,addressCache,specialApi);

            clear();
            blocks.add(block);
            transactions.addAll(block.getTransactions());
            block.setTransactions(null);

            Long txMaxId = 0L;
            if(!transactions.isEmpty()){
                // 查询交易信息补充表，补充缺失信息
                List<String> txHashes = new ArrayList<>();
                transactions.forEach(tx->txHashes.add(tx.getHash()));
                TxBakExample txBakExample = new TxBakExample();
                txBakExample.createCriteria().andHashIn(txHashes);
                List<TxBak> txBaks = txBakMapper.selectByExample(txBakExample);
                Map<String,TxBak> txBakMap = new HashMap<>();
                for (TxBak bak : txBaks) {
                    if(bak.getId()>txMaxId) txMaxId=bak.getId();
                    txBakMap.put(bak.getHash(), bak);
                }
                // 更新交易入库到ES和Redis的交易信息
                transactions.forEach(tx->{
                    TxBak bak = txBakMap.get(tx.getHash());
                    if(bak==null){
                        log.error("交易[{}]在交易备份表中找不到备份信息!",tx.getHash());
                        return;
                    }
                    BeanUtils.copyProperties(bak,tx);
                });
            }

            // 从数据库查询备份日志信息,补充到到ES/Redis
            Long nOptMaxId = 0L;
            Set<NodeOpt> nodeOpts = new HashSet<>();
            NOptBakExample nOptBakExample = new NOptBakExample();
            List<NOptBak> nOptBaks = nOptBakMapper.selectByExample(nOptBakExample);
            for (NOptBak bak : nOptBaks) {
                if(bak.getId()>nOptMaxId) nOptMaxId=bak.getId();
                NodeOpt no = ComplementNodeOpt.newInstance();
                BeanUtils.copyProperties(bak, no);
                nodeOpts.add(no);
            }

            esImportService.batchImport(blocks,transactions,nodeOpts,Collections.emptySet());
            redisImportService.batchImport(blocks,transactions, Collections.emptySet());
            // 更新已处理的最大ID, 方便删除任务删除已用完的数据
            BakDataDeleteUtil.updateTxBakMaxId(txMaxId);
            BakDataDeleteUtil.updateNOptBakMaxId(nOptMaxId);

            clear();
            event.getCallback().call(block.getNum());

            // 释放事件对对象的引用
            event.releaseRef();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }

    private void clear(){
        blocks.clear();
        transactions.clear();
    }
}