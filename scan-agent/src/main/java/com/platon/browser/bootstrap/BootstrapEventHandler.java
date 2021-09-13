package com.platon.browser.bootstrap;

import com.lmax.disruptor.EventHandler;
import com.platon.browser.analyzer.BlockAnalyzer;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.ReceiptResult;
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
import com.platon.browser.service.elasticsearch.EsImportService;
import com.platon.browser.service.redis.RedisImportService;
import com.platon.browser.utils.BakDataDeleteUtil;
import com.platon.browser.utils.CommonUtil;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 自检事件处理器
 */
@Slf4j
@Component
public class BootstrapEventHandler implements EventHandler<BootstrapEvent> {

    @Resource
    private EsImportService esImportService;

    @Resource
    private RedisImportService redisImportService;

    @Resource
    private TxBakMapper txBakMapper;

    @Resource
    private NOptBakMapper nOptBakMapper;

    @Resource
    private BlockAnalyzer blockAnalyzer;

    private Set<Block> blocks = new HashSet<>();

    private Set<Transaction> transactions = new HashSet<>();

    @Override
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE, label = "BootstrapEventHandler")
    public void onEvent(BootstrapEvent event, long sequence, boolean endOfBatch)
            throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException,
            ContractInvokeException, BlankResponseException {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(BootstrapEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException,
            ContractInvokeException, BlankResponseException {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.info("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(BootstrapEvent event, long sequence, boolean endOfBatch) throws ExecutionException, InterruptedException, BeanCreateOrUpdateException, IOException,
            ContractInvokeException, BlankResponseException {
        try {
            PlatonBlock.Block rawBlock = event.getBlockCF().get().getBlock();
            ReceiptResult receiptResult = event.getReceiptCF().get();
            CollectionBlock block = blockAnalyzer.analyze(rawBlock, receiptResult);

            this.clear();
            this.blocks.add(block);
            this.transactions.addAll(block.getTransactions());
            block.setTransactions(null);

            Long txMaxId = 0L;
            if (!this.transactions.isEmpty()) {
                // 查询交易信息补充表，补充缺失信息
                List<String> txHashes = new ArrayList<>();
                this.transactions.forEach(tx -> txHashes.add(tx.getHash()));
                TxBakExample txBakExample = new TxBakExample();
                txBakExample.createCriteria().andHashIn(txHashes);
                List<TxBak> txBaks = this.txBakMapper.selectByExample(txBakExample);
                Map<String, TxBak> txBakMap = new HashMap<>();
                for (TxBak bak : txBaks) {
                    if (bak.getId() > txMaxId)
                        txMaxId = bak.getId();
                    txBakMap.put(bak.getHash(), bak);
                }
                // 更新交易入库到ES和Redis的交易信息
                this.transactions.forEach(tx -> {
                    TxBak bak = txBakMap.get(tx.getHash());
                    if (bak == null) {
                        log.error("交易[{}]在交易备份表中找不到备份信息!", tx.getHash());
                        return;
                    }
                    BeanUtils.copyProperties(bak, tx);
                });
            }

            // 从数据库查询备份日志信息,补充到到ES/Redis
            Long nOptMaxId = 0L;
            Set<NodeOpt> nodeOpts = new HashSet<>();
            NOptBakExample nOptBakExample = new NOptBakExample();
            List<NOptBak> nOptBaks = this.nOptBakMapper.selectByExample(nOptBakExample);
            for (NOptBak bak : nOptBaks) {
                if (bak.getId() > nOptMaxId)
                    nOptMaxId = bak.getId();
                NodeOpt no = ComplementNodeOpt.newInstance();
                BeanUtils.copyProperties(bak, no);
                nodeOpts.add(no);
            }

            this.esImportService.batchImport(this.blocks, this.transactions, nodeOpts, Collections.emptySet());
            this.redisImportService.batchImport(this.blocks, this.transactions, Collections.emptySet());
            // 更新已处理的最大ID, 方便删除任务删除已用完的数据
            BakDataDeleteUtil.updateTxBakMaxId(txMaxId);
            BakDataDeleteUtil.updateNOptBakMaxId(nOptMaxId);

            this.clear();
            event.getCallback().call(block.getNum());

            // 释放事件对对象的引用
            event.releaseRef();
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }

    }

    private void clear() {
        this.blocks.clear();
        this.transactions.clear();
    }

}