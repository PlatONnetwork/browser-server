package com.platon.browser.bootstrap.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.platon.browser.analyzer.BlockAnalyzer;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.bootstrap.bean.SyncData;
import com.platon.browser.dao.custommapper.CustomTx20BakMapper;
import com.platon.browser.dao.custommapper.CustomTx721BakMapper;
import com.platon.browser.dao.custommapper.CustomTxBakMapper;
import com.platon.browser.dao.custommapper.CustomTxDelegationRewardBakMapper;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.elasticsearch.*;
import com.platon.browser.service.elasticsearch.bean.ESResult;
import com.platon.browser.service.elasticsearch.query.ESQueryBuilderConstructor;
import com.platon.browser.service.receipt.ReceiptService;
import com.platon.browser.service.redis.RedisImportService;
import com.platon.protocol.core.methods.response.PlatonBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * MySQL/ES/Redis启动一致性自检服务
 * 查询MySQL的最大id和ES的最大id做比较，以MySQL为准补充确实的数据
 * 缺陷：es并没有事务保证，es批量插入并不会按照id大小顺序入库，一批数据入库可能MaxId先入库，此时agent停止，则会丢失数据（结合优雅停机，可以比较大限度的避免此情形）
 *
 * @date: 2022/1/24
 */
@Slf4j
@Service
public class ConsistencyService {

    @Resource
    private NetworkStatMapper networkStatMapper;

    @Resource
    private TxBakMapper txBakMapper;

    @Resource
    private CustomTxBakMapper customTxBakMapper;

    @Resource
    private TxDelegationRewardBakMapper txDelegationRewardBakMapper;

    @Resource
    private CustomTxDelegationRewardBakMapper customTxDelegationRewardBakMapper;

    @Resource
    private TxErc20BakMapper txErc20BakMapper;

    @Resource
    private CustomTx20BakMapper customTx20BakMapper;

    @Resource
    private TxErc721BakMapper txErc721BakMapper;

    @Resource
    private CustomTx721BakMapper customTx721BakMapper;

    @Resource
    private EsImportService esImportService;

    @Resource
    private RedisImportService redisImportService;

    @Resource
    private BlockService blockService;

    @Resource
    private ReceiptService receiptService;

    @Resource
    private BlockAnalyzer blockAnalyzer;

    @Resource
    private EsBlockRepository esBlockRepository;

    @Resource
    private EsTransactionRepository esTransactionRepository;

    @Resource
    private EsDelegationRewardRepository esDelegationRewardRepository;

    @Resource
    private EsErc20TxRepository esErc20TxRepository;

    @Resource
    private EsErc721TxRepository esErc721TxRepository;

    /**
     * 开机自检,一致性开机自检子流程,检查es、redis中的区块高度和交易序号是否和mysql数据库一致，以mysql的数据为准
     *
     * @param traceId
     * @return void
     * @date 2021/4/19
     */
    @Transactional(rollbackFor = Exception.class)
    public void post(String traceId) throws Exception {
        NetworkStat networkStat = networkStatMapper.selectByPrimaryKey(1);
        if (networkStat == null) {
            return;
        }
        SyncData syncData = new SyncData();
        syncBlock(esBlockRepository, syncData, networkStat.getCurNumber());
        syncTx(esTransactionRepository, syncData);
        syncDelegationRewardTx(esDelegationRewardRepository, syncData);
        syncErc20Tx(esErc20TxRepository, syncData);
        syncErc721Tx(esErc721TxRepository, syncData);
        syncDataToESAndRedis(syncData);
        log.info("MYSQL/ES/REDIS中的数据同步完成!");
    }

    /**
     * 同步mysql的区块信息到es和Redis
     *
     * @param abstractEsRepository:
     * @param syncData:
     * @param mysqlBlockNum:
     * @return: void
     * @date: 2022/1/24
     */
    private void syncBlock(AbstractEsRepository abstractEsRepository, SyncData syncData, long mysqlBlockNum) throws Exception {
        ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
        constructor.setDesc("num");
        constructor.setResult(new String[]{"nodeId", "hash", "num"});
        constructor.setUnmappedType("long");
        ESResult<Block> queryResultFromES = abstractEsRepository.search(constructor, Block.class, 1, 1);
        List<Block> list = queryResultFromES.getRsData();
        long esBlockNum = CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getNum() : 0L;
        if (mysqlBlockNum > esBlockNum) {
            for (long number = esBlockNum; number <= mysqlBlockNum; number++) {
                // 异步获取区块
                CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(number);
                // 异步获取交易回执
                CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(number);
                PlatonBlock.Block rawBlock = blockCF.get().getBlock();
                ReceiptResult receiptResult = receiptCF.get();
                CollectionBlock block = blockAnalyzer.analyze(rawBlock, receiptResult);
                syncData.getBlockSet().add(block);
            }
            log.info("MYSQL/ES/REDIS块高数据同步区间:[{},{}]", esBlockNum, mysqlBlockNum);
        } else {
            log.info("MySQL没有块高数据需要同步");
        }
    }

    /**
     * 同步mysql的交易信息到es和Redis
     *
     * @param abstractEsRepository: es查询对象
     * @param syncData:             同步数据对象
     * @return: void
     * @date: 2022/1/24
     */
    private void syncTx(AbstractEsRepository abstractEsRepository, SyncData syncData) throws Exception {
        try {
            long mysqlTxId = customTxBakMapper.findMaxId();
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setDesc("id");
            constructor.setResult(new String[]{"id", "seq", "hash", "num"});
            constructor.setUnmappedType("long");
            ESResult<TxBak> queryResultFromES = abstractEsRepository.search(constructor, TxBak.class, 1, 1);
            List<TxBak> list = queryResultFromES.getRsData();
            long esTxId = CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getId() : 0L;
            if (mysqlTxId > esTxId) {
                TxBakExample example = new TxBakExample();
                example.createCriteria().andIdGreaterThan(esTxId).andIdLessThanOrEqualTo(mysqlTxId);
                List<TxBak> TxBakList = txBakMapper.selectByExample(example);
                for (TxBak txBak : TxBakList) {
                    Transaction transaction = new Transaction();
                    BeanUtil.copyProperties(txBak, transaction);
                    syncData.getTxBakSet().add(transaction);
                }
                log.info("MYSQL/ES/REDIS交易数据同步区间:[{},{}]", esTxId, mysqlTxId);
            } else {
                log.info("MySQL没有交易数据需要同步");
            }
        } catch (Exception e) {
            log.error("同步交易数据异常", e);
            throw new Exception("同步交易数据异常");
        }
    }

    /**
     * 同步mysql的领取交易信息到es和Redis
     *
     * @param abstractEsRepository:
     * @param syncData:
     * @return: void
     * @date: 2022/1/24
     */
    private void syncDelegationRewardTx(AbstractEsRepository abstractEsRepository, SyncData syncData) throws Exception {
        try {
            long mysqlTxId = customTxDelegationRewardBakMapper.findMaxId();
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setDesc("id");
            constructor.setResult(new String[]{"id", "hash", "bn"});
            constructor.setUnmappedType("long");
            ESResult<TxDelegationRewardBak> queryResultFromES = abstractEsRepository.search(constructor, TxDelegationRewardBak.class, 1, 1);
            List<TxDelegationRewardBak> list = queryResultFromES.getRsData();
            long esTxId = CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getId() : 0L;
            if (mysqlTxId > esTxId) {
                TxDelegationRewardBakExample example = new TxDelegationRewardBakExample();
                example.createCriteria().andIdGreaterThan(esTxId).andIdLessThanOrEqualTo(mysqlTxId);
                List<TxDelegationRewardBak> txDelegationRewardBakList = txDelegationRewardBakMapper.selectByExample(example);
                for (TxDelegationRewardBak txDelegationRewardBak : txDelegationRewardBakList) {
                    DelegationReward delegationReward = new DelegationReward();
                    BeanUtil.copyProperties(txDelegationRewardBak, delegationReward);
                    syncData.getDelegationRewardBakSet().add(delegationReward);
                }
                log.info("MYSQL/ES/REDIS领取奖励交易数据同步区间:[{},{}]", esTxId, mysqlTxId);
            } else {
                log.info("MySQL没有领取奖励交易数据需要同步");
            }
        } catch (Exception e) {
            log.error("MySQL同步领取奖励交易数据异常", e);
            throw new Exception("MySQL同步领取奖励交易数据异常");
        }
    }

    /**
     * 同步mysql的erc20交易信息到es和Redis
     *
     * @param abstractEsRepository:
     * @param syncData:
     * @return: void
     * @date: 2022/1/24
     */
    private void syncErc20Tx(AbstractEsRepository abstractEsRepository, SyncData syncData) throws Exception {
        try {
            long mysqlTxId = customTx20BakMapper.findMaxId();
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setDesc("id");
            constructor.setResult(new String[]{"id", "seq", "hash", "bn"});
            constructor.setUnmappedType("long");
            ESResult<ErcTx> queryResultFromES = abstractEsRepository.search(constructor, ErcTx.class, 1, 1);
            List<ErcTx> list = queryResultFromES.getRsData();
            long esTxId = CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getId() : 0L;
            if (mysqlTxId > esTxId) {
                TxErc20BakExample example = new TxErc20BakExample();
                example.createCriteria().andIdGreaterThan(esTxId).andIdLessThanOrEqualTo(mysqlTxId);
                List<TxErc20Bak> txErc20BakList = txErc20BakMapper.selectByExample(example);
                for (TxErc20Bak txErc20Bak : txErc20BakList) {
                    ErcTx ercTx = new ErcTx();
                    BeanUtil.copyProperties(txErc20Bak, ercTx);
                    syncData.getErc20BakSet().add(ercTx);
                }
                log.info("MYSQL/ES/REDIS erc20交易数据同步区间:[{},{}]", esTxId, mysqlTxId);
            } else {
                log.info("MySQL没有erc20交易数据需要同步");
            }
        } catch (Exception e) {
            log.error("MySQL同步erc20交易数据异常", e);
            throw new Exception("MySQL同步erc20交易数据异常");
        }
    }

    /**
     * 同步mysql的erc721交易信息到es和Redis
     *
     * @param abstractEsRepository:
     * @param syncData:
     * @return: void
     * @date: 2022/1/24
     */
    private void syncErc721Tx(AbstractEsRepository abstractEsRepository, SyncData syncData) throws Exception {
        try {
            long mysqlTxId = customTx721BakMapper.findMaxId();
            ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
            constructor.setDesc("id");
            constructor.setResult(new String[]{"id", "seq", "hash", "bn"});
            constructor.setUnmappedType("long");
            ESResult<ErcTx> queryResultFromES = abstractEsRepository.search(constructor, ErcTx.class, 1, 1);
            List<ErcTx> list = queryResultFromES.getRsData();
            long esTxId = CollUtil.isNotEmpty(list) ? CollUtil.getFirst(list).getId() : 0L;
            if (mysqlTxId > esTxId) {
                TxErc721BakExample example = new TxErc721BakExample();
                example.createCriteria().andIdGreaterThan(esTxId).andIdLessThanOrEqualTo(mysqlTxId);
                List<TxErc721Bak> txErc721BakList = txErc721BakMapper.selectByExample(example);
                for (TxErc721Bak txErc721Bak : txErc721BakList) {
                    ErcTx ercTx = new ErcTx();
                    BeanUtil.copyProperties(txErc721Bak, ercTx);
                    syncData.getErc20BakSet().add(ercTx);
                }
                log.info("MYSQL/ES/REDIS erc721交易数据同步区间:[{},{}]", esTxId, mysqlTxId);
            } else {
                log.info("MySQL没有erc721交易数据需要同步");
            }
        } catch (Exception e) {
            log.error("MySQL同步erc721交易数据异常", e);
            throw new Exception("MySQL同步erc721交易数据异常");
        }
    }

    /**
     * 同步交易数据到es和Redis
     *
     * @param syncData:
     * @return: void
     * @date: 2022/1/20
     */
    private void syncDataToESAndRedis(SyncData syncData) throws Exception {
        esImportService.batchImport(syncData.getBlockSet(), syncData.getTxBakSet(), syncData.getErc20BakSet(), syncData.getErc721BakSet(), syncData.getDelegationRewardBakSet());
        redisImportService.batchImport(syncData.getBlockSet(), syncData.getTxBakSet(), syncData.getErc20BakSet(), syncData.getErc721BakSet());
    }

}
