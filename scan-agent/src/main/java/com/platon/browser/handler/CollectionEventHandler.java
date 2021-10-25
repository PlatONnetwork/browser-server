package com.platon.browser.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.analyzer.TransactionAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.CollectionTransaction;
import com.platon.browser.bean.Receipt;
import com.platon.browser.bean.TxAnalyseResult;
import com.platon.browser.cache.AddressCache;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.dao.custommapper.CustomNOptBakMapper;
import com.platon.browser.dao.custommapper.CustomTxBakMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NOptBakMapper;
import com.platon.browser.dao.mapper.TxBakMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.publisher.ComplementEventPublisher;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.ppos.PPOSService;
import com.platon.browser.service.statistic.StatisticService;
import com.platon.browser.utils.BakDataDeleteUtil;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 区块事件处理器
 */
@Slf4j
@Component
public class CollectionEventHandler implements EventHandler<CollectionEvent> {

    @Resource
    private PPOSService pposService;

    @Resource
    private BlockService blockService;

    @Resource
    private StatisticService statisticService;

    @Resource
    private ComplementEventPublisher complementEventPublisher;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private NOptBakMapper nOptBakMapper;

    @Resource
    private CustomNOptBakMapper customNOptBakMapper;

    @Resource
    private TxBakMapper txBakMapper;

    @Resource
    private CustomTxBakMapper customTxBakMapper;

    @Resource
    private AddressCache addressCache;

    @Resource
    private TransactionAnalyzer transactionAnalyzer;

    // 交易序号id
    private long transactionId = 0;

    private long txDeleteBatchCount = 0;

    private long optDeleteBatchCount = 0;

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Transactional
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        surroundExec(event, sequence, endOfBatch);
    }

    private void surroundExec(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        resetBlock(event);
        // 之前在BlockEventHandler中的交易分析逻辑挪至当前位置 START
        Map<String, Receipt> receiptMap = event.getBlock().getReceiptMap();
        List<com.platon.protocol.core.methods.response.Transaction> rawTransactions = event.getBlock().getOriginTransactions();
        for (com.platon.protocol.core.methods.response.Transaction tr : rawTransactions) {
            CollectionTransaction transaction = transactionAnalyzer.analyze(event.getBlock(), tr, receiptMap.get(tr.getHash()));
            // 把解析好的交易添加到当前区块的交易列表
            event.getBlock().getTransactions().add(transaction);
            // 设置当前块的erc20交易数和erc721u交易数，以便更新network_stat表
            event.getBlock().setErc20TxQty(event.getBlock().getErc20TxQty() + transaction.getErc20TxList().size());
            event.getBlock().setErc721TxQty(event.getBlock().getErc721TxQty() + transaction.getErc721TxList().size());
        }
        // 之前在BlockEventHandler中的交易分析逻辑挪至当前位置 END

        // 使用已入库的交易数量初始化交易ID初始值
        if (transactionId == 0) transactionId = networkStatCache.getNetworkStat().getTxQty();

        try {
            List<Transaction> transactions = event.getTransactions();
            // 确保交易从小到大的索引顺序
            transactions.sort(Comparator.comparing(Transaction::getIndex));
            for (Transaction tx : transactions) {
                tx.setId(++transactionId);
            }

            // 根据区块号解析出业务参数
            List<NodeOpt> nodeOpts1 = blockService.analyze(event);
            // 根据交易解析出业务参数
            TxAnalyseResult txAnalyseResult = pposService.analyze(event);
            // 统计业务参数
            statisticService.analyze(event);
            if (!txAnalyseResult.getNodeOptList().isEmpty()) nodeOpts1.addAll(txAnalyseResult.getNodeOptList());

            txDeleteBatchCount++;
            optDeleteBatchCount++;

            if (txDeleteBatchCount >= 10) {
                // 删除小于最高ID的交易备份
                TxBakExample txBakExample = new TxBakExample();
                txBakExample.createCriteria().andIdLessThanOrEqualTo(BakDataDeleteUtil.getTxBakMaxId());
                int txCount = txBakMapper.deleteByExample(txBakExample);
                log.debug("清除交易备份记录({})条", txCount);
                txDeleteBatchCount = 0;
            }
            // 交易入库mysql
            if (!transactions.isEmpty()) {
                List<TxBak> baks = new ArrayList<>();
                transactions.forEach(tx -> {
                    TxBak bak = new TxBak();
                    BeanUtils.copyProperties(tx, bak);
                    baks.add(bak);
                });
                customTxBakMapper.batchInsertOrUpdateSelective(baks, TxBak.Column.values());
            }

            if (optDeleteBatchCount >= 10) {
                // 删除小于最高ID的操作记录备份
                NOptBakExample nOptBakExample = new NOptBakExample();
                nOptBakExample.createCriteria().andIdLessThanOrEqualTo(BakDataDeleteUtil.getNOptBakMaxId());
                int optCount = nOptBakMapper.deleteByExample(nOptBakExample);
                log.debug("清除操作备份记录({})条", optCount);
                optDeleteBatchCount = 0;
            }
            // 操作日志入库mysql
            if (!nodeOpts1.isEmpty()) {
                List<NOptBak> baks = new ArrayList<>();
                nodeOpts1.forEach(no -> {
                    NOptBak bak = new NOptBak();
                    BeanUtils.copyProperties(no, bak);
                    baks.add(bak);
                });
                customNOptBakMapper.batchInsertOrUpdateSelective(baks, NOptBak.Column.values());
            }
            complementEventPublisher.publish(event.getBlock(), transactions, nodeOpts1, txAnalyseResult.getDelegationRewardList(), event.getTraceId());
            // 释放对象引用
            event.releaseRef();
            retryCount.set(0);
        } catch (Exception e) {
            log.error(StrUtil.format("区块[{}]解析交易异常", event.getBlock().getNum()), e);
            throw e;
        } finally {
            log.info("清除地址缓存[addressCache]数据[{}]条,addressCache:({})",
                     addressCache.getAll().size(),
                     JSONUtil.toJsonStr(addressCache.getAll().stream().map(Address::getAddress).collect(Collectors.toList())));
            // 当前事务不管是正常处理结束或异常结束，都需要重置地址缓存，防止代码中任何地方出问题后，缓存中留存脏数据
            // 因为地址缓存是当前事务处理的增量缓存，在 StatisticsAddressAnalyzer 进行数据合并入库时：
            // 1、如果出现异常，由于事务保证，当前事务统计的地址数据不会入库mysql，此时应该清空增量缓存，等待下次重试时重新生成缓存
            // 2、如果正常结束，当前事务统计的地址数据会入库mysql，此时应该清空增量缓存
            addressCache.cleanAll();
        }
    }

    /**
     * 避免重试机制会重复统计
     *
     * @param event:
     * @return: void
     * @date: 2021/10/18
     */
    private void resetBlock(CollectionEvent event) {
        if (CollUtil.isNotEmpty(event.getBlock().getTransactions())) {
            event.getBlock().getTransactions().clear();
        }
        if (retryCount.incrementAndGet() > 1) {
            event.getBlock().setErc20TxQty(0).setErc721TxQty(0).setTxFee("0").setDQty(0).setPQty(0).setSQty(0).setTranQty(0).setTxQty(0);
            List<String> txHashList = CollUtil.newArrayList();
            if (CollUtil.isNotEmpty(event.getBlock().getOriginTransactions())) {
                txHashList = event.getBlock().getOriginTransactions().stream().map(com.platon.protocol.core.methods.response.Transaction::getHash).collect(Collectors.toList());
            }
            log.error("重试次数[{}],该区块[{}]交易列表{}重复处理，可能会引起数据重复统计，event对象数据为[{}]", retryCount.get(), event.getBlock().getNum(), JSONUtil.toJsonStr(txHashList), JSONUtil.toJsonStr(event));
        }
    }

}
