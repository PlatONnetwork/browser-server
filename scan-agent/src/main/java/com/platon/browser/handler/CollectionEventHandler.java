package com.platon.browser.handler;

import cn.hutool.core.collection.CollUtil;
import com.lmax.disruptor.EventHandler;
import com.platon.browser.analyzer.TransactionAnalyzer;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.Receipt;
import com.platon.browser.bean.TxAnalyseResult;
import com.platon.browser.cache.NewAddressCache;
import com.platon.browser.cache.NodeCache;
import com.platon.browser.dao.custommapper.*;
import com.platon.browser.dao.entity.TxTransferBak;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.publisher.ComplementEventPublisher;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.ppos.PPOSService;
import com.platon.browser.service.statistic.StatisticService;
import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
    private CustomNOptBakMapper customNOptBakMapper;

    @Resource
    private NodeMapper nodeMapper;

    @Resource
    private CustomTxBakMapper customTxBakMapper;

    @Resource
    private NewAddressCache newAddressCache;

    @Resource
    private NodeCache nodeCache;

    @Resource
    private TransactionAnalyzer transactionAnalyzer;

    @Resource
    private CustomTx20BakMapper customTx20BakMapper;

    @Resource
    private CustomTx721BakMapper customTx721BakMapper;

    @Resource
    private CustomTx1155BakMapper customTx1155BakMapper;

    @Resource
    private CustomTxDelegationRewardBakMapper customTxDelegationRewardBakMapper;

    @Resource
    private CustomTokenHolderMapper customTokenHolderMapper;

    @Resource
    private CustomToken1155HolderMapper customToken1155HolderMapper;

    @Resource
    private CustomTxTransferBakMapper customTxTransferBakMapper;

    /**
     * 重试次数
     */
    private AtomicLong retryCount = new AtomicLong(0);

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void onEvent(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        surroundExec(event, sequence, endOfBatch);
    }

    /**
     * 重试完成还是不成功，会回调该方法
     *
     * @param e:
     * @return: void
     * @date: 2022/5/6
     */
    @Recover
    public void recover(Exception e) {
        retryCount.set(0);
        log.error("重试完成还是业务失败，请联系管理员处理");
    }

    private void surroundExec(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        CommonUtil.putTraceId(event.getTraceId());
        long startTime = System.currentTimeMillis();
        exec(event, sequence, endOfBatch);
        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);
        CommonUtil.removeTraceId();
    }

    private void exec(CollectionEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 确保event是原始副本，重试机制每一次使用的都是copyEvent
        event.getBlock().setErc20TxQty(0);
        event.getBlock().setErc721TxQty(0);
        event.getBlock().setErc1155TxQty(0);
        event.getBlock().getDtoTransactions().clear();

        newAddressCache.clearBlockRelatedAddressCache();

        log.info("开始分析CollectionEvent，块高：{}", event.getBlock().getNum());

        StopWatch watch = new StopWatch("分析CollectionEvent");
        try {
            Map<String, Receipt> receiptMap = event.getBlock().getReceiptMap();
            List<com.platon.protocol.core.methods.response.Transaction> rawTransactions = event.getBlock().getOriginTransactions();
            for (com.platon.protocol.core.methods.response.Transaction rawTx : rawTransactions) {
                watch.start("分析Transaction");
                //
                // 重要：
                // 解析区块中的交易，特别是token交易，按token类型分别解析出交易信息，放入各自类型的列表中；并把token的holder，以及holder持有token的余额，交易次数等信息，保存到本地db中
                com.platon.browser.elasticsearch.dto.Transaction dtoTransaction = transactionAnalyzer.analyze(event.getBlock(), rawTx, receiptMap.get(rawTx.getHash()));
                watch.stop();
                // 把解析好的交易添加到当前区块的交易列表（交易类型已经设置号）
                event.getBlock().getDtoTransactions().add(dtoTransaction);
                // 设置当前块的erc20交易数和erc721u交易数，以便更新network_stat表
                event.getBlock().setErc20TxQty(event.getBlock().getErc20TxQty() + dtoTransaction.getErc20TxList().size());
                event.getBlock().setErc721TxQty(event.getBlock().getErc721TxQty() + dtoTransaction.getErc721TxList().size());
                event.getBlock().setErc1155TxQty(event.getBlock().getErc1155TxQty() + dtoTransaction.getErc1155TxList().size());
            }

            watch.start("排序Transaction");
            List<com.platon.browser.elasticsearch.dto.Transaction> transactions = event.getBlock().getDtoTransactions();
            // 确保交易从小到大的索引顺序
            transactions.sort(Comparator.comparing(com.platon.browser.elasticsearch.dto.Transaction::getIndex));
            watch.stop();
            // 根据区块号解析出业务参数
            watch.start("分析NodeOpt");
            //2023/04/07 lvixaoyi  没有影响输入参数的值
            List<NodeOpt> nodeOpts1 = blockService.analyze(event);
            // 根据交易解析出业务参数
            watch.stop();
            watch.start("分析ppos");
            //2023/04/07 lvixaoyi  入参event.transactions中的每个对象会被设置seq, txInfo值，并且会设置addressCache
            //但是这些对如此的改变，即使是重复执行，也没有关系，都是重置操作（没有增量操作）
            TxAnalyseResult txAnalyseResult = pposService.analyze(event);
            watch.stop();
            // 汇总操作记录
            if (CollUtil.isNotEmpty(txAnalyseResult.getNodeOptList())) {
                nodeOpts1.addAll(txAnalyseResult.getNodeOptList());
            }

            // 统计业务参数，以MySQL数据库块高为准，所以必须保证块高是最后入库
            // 2023/04/07, lvxiaoyi     保证新地址入库在前，其相关的交易入库在后，这样，tx_bak的trigger才能正确更新新地址的交易数量。
            // 2023/04/07 lvixaoyi  没有影响输入参数的值
            watch.start("统计业务参数");
            statisticService.analyze(event);
            watch.stop();

            // 这里把解析出的所有数据详情保存到本地DB。
            // 这样即使后续ES入库失败，只需要重启agent，有com.platon.browser.bootstrap.service.ConsistencyService来完成ES重新入库，而不需要再从链上同步
            // 交易入库mysql，因为缓存无法实现自增id，不再删除操作日志表
            List<ErcTx> erc20List = new ArrayList<>();
            List<ErcTx> erc721List = new ArrayList<>();
            List<ErcTx> erc1155List = new ArrayList<>();
            List<TxTransferBak> embedTransferTxList = new ArrayList<>();
            this.getErcTx(transactions, erc20List, erc721List, erc1155List,embedTransferTxList);
            if (CollUtil.isNotEmpty(transactions)) {
                // 依赖于数据库的自增id
                watch.start("入库TxBak");
                customTxBakMapper.batchInsertOrUpdateSelective(transactions);
                watch.stop();
            }

            if (CollUtil.isNotEmpty(erc20List)) {
                watch.start("入库Tx20Bak");
                customTx20BakMapper.batchInsert(erc20List);
                watch.stop();
            }
            if (CollUtil.isNotEmpty(erc721List)) {
                watch.start("入库Tx721Bak");
                customTx721BakMapper.batchInsert(erc721List);
                watch.stop();
            }
            if (CollUtil.isNotEmpty(erc1155List)) {
                watch.start("入库Tx1155Bak");
                customTx1155BakMapper.batchInsert(erc1155List);
                watch.stop();
            }
            if (CollUtil.isNotEmpty(embedTransferTxList)) {
                watch.start("入库TxTransferBak");
                customTxTransferBakMapper.batchInsert(embedTransferTxList);
                watch.stop();
            }
            List<DelegationReward> delegationRewardList = txAnalyseResult.getDelegationRewardList();
            // 委托奖励交易入库
            if (CollUtil.isNotEmpty(delegationRewardList)) {
                watch.start("入库TxDelegation");
                customTxDelegationRewardBakMapper.batchInsert(delegationRewardList);
                watch.stop();
            }
            // 操作日志入库mysql，再由定时任务同步到es，因为缓存无法实现自增id，所以不再由环形队列入库，不再删除操作日志表
            if (CollUtil.isNotEmpty(nodeOpts1)) {
                // 依赖于数据库的自增id
                watch.start("入库nodeOpts");
                customNOptBakMapper.batchInsertOrUpdateSelective(nodeOpts1);
                watch.stop();
            }

            // TODO 此分割线以上代码异常重试属于正常逻辑，如果是以下代码发生异常，可能区块相关交易已经发送到ComplementEventHandler进行处理，则该区块会被重复处理多次
            watch.start("发布complementEvent");
            complementEventPublisher.publish(event.getBlock(), (List<Transaction>)transactions, nodeOpts1, delegationRewardList, event.getTraceId());
            watch.stop();
            log.info("结束分析CollectionEvent，块高：{}，耗时统计：{}", event.getBlock().getNum(), watch.prettyPrint());
            // 释放对象引用
            event.releaseRef();
            retryCount.set(0);
        } catch (Exception e) {
            log.error("区块：{} 解析交易异常", event.getBlock().getNum(), e);
            log.error("区块解析交易异常", e);
            throw e;
        }
        /*
        2023/04/12 lvxiaoyi, 进入时清理缓存，参考：com.platon.browser.cache.NewAddressCache.clearBlockRelatedAddressCache
        finally {
            // 当前事务不管是正常处理结束或异常结束，都需要重置地址缓存，防止代码中任何地方出问题后，缓存中留存脏数据
            // 因为地址缓存是当前事务处理的增量缓存，在 StatisticsAddressAnalyzer 进行数据合并入库时：
            // 1、如果出现异常，由于事务保证，当前事务统计的地址数据不会入库mysql，此时应该清空增量缓存，等待下次重试时重新生成缓存
            // 2、如果正常结束，当前事务统计的地址数据会入库mysql，此时应该清空增量缓存
            addressCache.cleanAll();
        }*/
    }

    /**
     * 模拟深拷贝
     * 因为CollectionEvent引用了第三方的jar对象，没有实现系列化接口，没法做深拷贝
     *
     * @param event:
     * @return: com.platon.browser.bean.CollectionEvent
     * @date: 2021/11/22
     */
    /*private CollectionEvent copyCollectionEvent(CollectionEvent event) {
        CollectionEvent copyEvent = new CollectionEvent();
        Block block = new Block();
        BeanUtil.copyProperties(event.getBlock(), block);
        copyEvent.setBlock(block);
        copyEvent.getTransactions().addAll(event.getTransactions());
        EpochMessage epochMessage = EpochMessage.newInstance();
        BeanUtil.copyProperties(event.getEpochMessage(), epochMessage);
        copyEvent.setEpochMessage(epochMessage);
        copyEvent.setTraceId(event.getTraceId());
        if (retryCount.incrementAndGet() > 1) {
            initNodeCache();
            List<String> txHashList = CollUtil.newArrayList();
            if (CollUtil.isNotEmpty(event.getBlock().getOriginTransactions())) {
                txHashList = event.getBlock().getOriginTransactions().stream().map(com.platon.protocol.core.methods.response.Transaction::getHash).collect(Collectors.toList());
            }
            log.warn("重试次数[{}],节点重新初始化，该区块[{}]交易列表{}重复处理", retryCount.get(), event.getBlock().getNum(), JSONUtil.toJsonStr(txHashList));
        }
        return copyEvent;
    }*/

    /**
     * 初始化节点缓存
     *
     * @param :
     * @return: void
     * @date: 2021/11/30
     */
    private void initNodeCache() {
        nodeCache.cleanNodeCache();
        List<com.platon.browser.dao.entity.Node> nodeList = nodeMapper.selectByExample(null);
        nodeCache.init(nodeList);
    }

    /**
     * erc20交易入库
     *
     * @param transactions:
     * @return: void
     * @date: 2021/12/16
     */
    /*private void addTxErc20Bak(List<Transaction> transactions) {
        List<ErcTx> erc20List = new ArrayList<>();
        transactions.forEach(transaction -> {
            if (CollUtil.isNotEmpty(transaction.getErc20TxList())) {
                erc20List.addAll(transaction.getErc20TxList());
            }
        });
        if (CollUtil.isNotEmpty(erc20List)) {
            customTx20BakMapper.batchInsert(erc20List);
        }
    }*/


    private void getErcTx(List<com.platon.browser.elasticsearch.dto.Transaction> transactions, List<ErcTx> erc20List, List<ErcTx> erc721List, List<ErcTx>erc1155List, List<TxTransferBak> embedTransferTxList){
        transactions.forEach(transaction -> {
            if (CollUtil.isNotEmpty(transaction.getErc20TxList())) {
                erc20List.addAll(transaction.getErc20TxList());
            }
            if (CollUtil.isNotEmpty(transaction.getErc721TxList())) {
                erc721List.addAll(transaction.getErc721TxList());
            }
            if (CollUtil.isNotEmpty(transaction.getErc1155TxList())) {
                erc1155List.addAll(transaction.getErc1155TxList());
            }
            if (CollUtil.isNotEmpty(transaction.getTransferTxList())) {
                embedTransferTxList.addAll(transaction.getTransferTxList());
            }
        });
    }

    /**
     * erc721交易入库
     *
     * @param transactions:
     * @return: void
     * @date: 2021/12/16
     */
    /*private void addTxErc721Bak(List<Transaction> transactions) {
        List<ErcTx> erc721List = new ArrayList<>();
        transactions.forEach(transaction -> {
            if (CollUtil.isNotEmpty(transaction.getErc721TxList())) {
                erc721List.addAll(transaction.getErc721TxList());
            }
        });
        if (CollUtil.isNotEmpty(erc721List)) {
            customTx721BakMapper.batchInsert(erc721List);
        }
    }*/

    /**
     * erc1155交易入库
     *
     * @param transactions:
     * @return: void
     * @date: 2022/2/5
     */
   /* private void addTxErc1155Bak(List<Transaction> transactions) {
        List<ErcTx> erc1155Set = new ArrayList<>();
        transactions.forEach(transaction -> {
            if (CollUtil.isNotEmpty(transaction.getErc1155TxList())) {
                erc1155Set.addAll(transaction.getErc1155TxList());
            }
        });
        if (CollUtil.isNotEmpty(erc1155Set)) {
            customTx1155BakMapper.batchInsert(erc1155Set);
        }
    }*/
}
