package com.platon.browser.bootstrap.service;


import cn.hutool.core.bean.BeanUtil;
import com.platon.browser.analyzer.BlockAnalyzer;
import com.platon.browser.bean.CollectionBlock;
import com.platon.browser.bean.ReceiptResult;
import com.platon.browser.bootstrap.bean.SyncData;
import com.platon.browser.config.DisruptorConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.elasticsearch.dto.DelegationReward;
import com.platon.browser.elasticsearch.dto.ErcTx;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.service.block.BlockService;
import com.platon.browser.service.elasticsearch.EsImportService;
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
 * @description: MySQL/ES/Redis启动一致性自检服务
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-06 10:10:30
 **/
@Slf4j
@Service
public class ConsistencyService {

    @Resource
    private NetworkStatMapper networkStatMapper;

    @Resource
    private TxBakMapper txBakMapper;

    @Resource
    private TxDelegationRewardBakMapper txDelegationRewardBakMapper;

    @Resource
    private TxErc20BakMapper txErc20BakMapper;

    @Resource
    private TxErc721BakMapper txErc721BakMapper;

    @Resource
    private EsImportService esImportService;

    @Resource
    private RedisImportService redisImportService;

    @Resource
    private BlockService blockService;

    @Resource
    private ReceiptService receiptService;

    @Resource
    private DisruptorConfig disruptorConfig;

    @Resource
    private BlockAnalyzer blockAnalyzer;

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
        // mysql中的最高块号
        long mysqlMaxBlockNum = networkStat.getCurNumber();
        // 开始补的块高，重复的数据，Redis会过滤，ES会覆盖
        long startBlockNum = 1;
        if (mysqlMaxBlockNum > disruptorConfig.getPersistenceBatchSize()) {
            startBlockNum = mysqlMaxBlockNum - disruptorConfig.getPersistenceBatchSize();
        }
        log.info("MYSQL/ES/REDIS数据同步区间:[{},{}]", startBlockNum, mysqlMaxBlockNum);
        // 补充 [startBlockNum,mysqlMaxBlockNum] 闭区间内的区块和交易信息到ES和Redis
        SyncData syncData = new SyncData();
        syncBlock(syncData, startBlockNum, mysqlMaxBlockNum);
        syncTx(syncData, startBlockNum, mysqlMaxBlockNum);
        syncDelegationRewardTx(syncData, startBlockNum, mysqlMaxBlockNum);
        syncErc20Tx(syncData, startBlockNum, mysqlMaxBlockNum);
        syncErc721Tx(syncData, startBlockNum, mysqlMaxBlockNum);
        syncDataToESAndRedis(syncData);
        log.info("MYSQL/ES/REDIS中的数据同步完成!");
    }

    /**
     * 同步mysql的区块信息到es和Redis
     *
     * @param syncData:      同步数据对象
     * @param startBlockNum: 开始块高
     * @param endBlockNum:   结束块高
     * @return: void
     * @date: 2022/1/19
     */
    private void syncBlock(SyncData syncData, long startBlockNum, long endBlockNum) throws Exception {
        for (long number = startBlockNum; number <= endBlockNum; number++) {
            // 异步获取区块
            CompletableFuture<PlatonBlock> blockCF = blockService.getBlockAsync(number);
            // 异步获取交易回执
            CompletableFuture<ReceiptResult> receiptCF = receiptService.getReceiptAsync(number);
            PlatonBlock.Block rawBlock = blockCF.get().getBlock();
            ReceiptResult receiptResult = receiptCF.get();
            CollectionBlock block = blockAnalyzer.analyze(rawBlock, receiptResult);
            syncData.getBlockSet().add(block);
        }
    }

    /**
     * 同步mysql的交易信息到es和Redis
     *
     * @param syncData:      同步数据对象
     * @param startBlockNum: 开始块高
     * @param endBlockNum:   结束块高
     * @return: void
     * @date: 2022/1/19
     */
    private void syncTx(SyncData syncData, long startBlockNum, long endBlockNum) {
        TxBakExample example = new TxBakExample();
        example.createCriteria().andNumGreaterThanOrEqualTo(startBlockNum).andNumLessThanOrEqualTo(endBlockNum);
        List<TxBak> TxBakList = txBakMapper.selectByExample(example);
        for (TxBak txBak : TxBakList) {
            Transaction transaction = new Transaction();
            BeanUtil.copyProperties(txBak, transaction);
            syncData.getTxBakSet().add(transaction);
        }
    }

    /**
     * 同步mysql的领取交易信息到es和Redis
     *
     * @param syncData:
     * @param startBlockNum:
     * @param endBlockNum:
     * @return: void
     * @date: 2022/1/20
     */
    private void syncDelegationRewardTx(SyncData syncData, long startBlockNum, long endBlockNum) {
        TxDelegationRewardBakExample example = new TxDelegationRewardBakExample();
        example.createCriteria().andBnGreaterThanOrEqualTo(startBlockNum).andBnLessThanOrEqualTo(endBlockNum);
        List<TxDelegationRewardBak> txDelegationRewardBakList = txDelegationRewardBakMapper.selectByExample(example);
        for (TxDelegationRewardBak txDelegationRewardBak : txDelegationRewardBakList) {
            DelegationReward delegationReward = new DelegationReward();
            BeanUtil.copyProperties(txDelegationRewardBak, delegationReward);
            syncData.getDelegationRewardBakSet().add(delegationReward);
        }
    }

    /**
     * 同步mysql的erc20交易信息到es和Redis
     *
     * @param syncData:
     * @param startBlockNum:
     * @param endBlockNum:
     * @return: void
     * @date: 2022/1/20
     */
    private void syncErc20Tx(SyncData syncData, long startBlockNum, long endBlockNum) {
        TxErc20BakExample example = new TxErc20BakExample();
        example.createCriteria().andBnGreaterThanOrEqualTo(startBlockNum).andBnLessThanOrEqualTo(endBlockNum);
        List<TxErc20Bak> txErc20BakList = txErc20BakMapper.selectByExample(example);
        for (TxErc20Bak txErc20Bak : txErc20BakList) {
            ErcTx ercTx = new ErcTx();
            BeanUtil.copyProperties(txErc20Bak, ercTx);
            syncData.getErc20BakSet().add(ercTx);
        }
    }

    /**
     * 同步mysql的erc721交易信息到es和Redis
     *
     * @param syncData:
     * @param startBlockNum:
     * @param endBlockNum:
     * @return: void
     * @date: 2022/1/20
     */
    private void syncErc721Tx(SyncData syncData, long startBlockNum, long endBlockNum) {
        TxErc721BakExample example = new TxErc721BakExample();
        example.createCriteria().andBnGreaterThanOrEqualTo(startBlockNum).andBnLessThanOrEqualTo(endBlockNum);
        List<TxErc721Bak> txErc721BakList = txErc721BakMapper.selectByExample(example);
        for (TxErc721Bak txErc721Bak : txErc721BakList) {
            ErcTx ercTx = new ErcTx();
            BeanUtil.copyProperties(txErc721Bak, ercTx);
            syncData.getErc20BakSet().add(ercTx);
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
