package com.platon.browser.collection.service.epoch;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.common.service.AccountService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 周期切换服务
 *
 * 1、根据区块号计算周期切换相关值：
 *      名称/含义                                                                   变量名称
 * a、当前所处共识周期轮数                                                         consensusEpochRound
 * b、当前所处结算周期轮数                                                         settleEpochRound
 * c、当前所处结算周期轮数                                                         issueEpochRound
 *    当前增发周期开始时的激励池余额 IB                                             inciteBalance
 *    当前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例               inciteAmount4Block
 *    当前增发周期每个区块奖励值 BR/增发周期区块总数                                 blockReward
 *    当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例               inciteAmount4Stake
 *    当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数        settleStakeReward
 *    当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数         stakeReward
 */
@Slf4j
@Service
public class EpochService {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private PlatOnClient platOnClient;
    @Autowired
    private SpecialContractApi specialContractApi;
    @Autowired
    private AccountService accountService;

    @Getter private BigInteger currentBlockNumber; // 当前区块号
    @Getter private long consensusEpochRound; // 当前所处共识周期轮数
    @Getter private long settleEpochRound; // 当前所处结算周期轮数
    @Getter private long issueEpochRound; // 当前所处结算周期轮数
    @Getter private BigInteger inciteBalance; // 当前增发周期开始时的激励池余额 IB
    @Getter private BigInteger inciteAmount4Block; // 前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例
    @Getter private BigInteger blockReward; // 当前增发周期每个区块奖励值 BR/增发周期区块总数
    @Getter private BigInteger inciteAmount4Stake; // 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例
    @Getter private BigInteger settleStakeReward;  // 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数
    @Getter private BigInteger stakeReward; // 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数

    /**
     * 使用区块号更新服务内部状态
     * @param blockNumber
     */
    public void update(BigInteger blockNumber) {
        this.currentBlockNumber=blockNumber;

        // 暂存未计算前的增发周期值
        long oldIssueEpochRound = this.issueEpochRound;
        // 计算共识周期轮数
        long oldConsensusEpochRound = this.consensusEpochRound;
        this.consensusEpochRound=new BigDecimal(blockNumber).divide(new BigDecimal(chainConfig.getConsensusPeriodBlockCount()), 0, RoundingMode.CEILING).longValue();
        if(oldConsensusEpochRound!=this.consensusEpochRound){
            // 如果共识周期与根据当前区块号算出来的不一致，则需要重新计算结算周期
            long oldSettleEpochRound = this.settleEpochRound;
            this.settleEpochRound=new BigDecimal(blockNumber).divide(new BigDecimal(chainConfig.getSettlePeriodBlockCount()), 0, RoundingMode.CEILING).longValue();
            if(oldSettleEpochRound!=this.settleEpochRound){
                // 如果结算周期与根据当前区块号算出来的不一致，则需要重新计算增发周期
                this.issueEpochRound=new BigDecimal(blockNumber).divide(new BigDecimal(chainConfig.getAddIssuePeriodBlockCount()), 0, RoundingMode.CEILING).longValue();
            }
        }

        if(oldIssueEpochRound!=this.issueEpochRound){
            // 如果增发周期变更,则更新相应的奖励字段
            // 当前增发周期的初始激励池余额需要在上一增发周期最后一个块时候确定
            try {
                // 上一增发周期最后一个块号
                BigInteger preIssueEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(blockNumber,chainConfig.getAddIssuePeriodBlockCount().longValue());
                // 当前增发周期开始时的激励池余额
                BigInteger newInciteBalance = accountService.getInciteBalance(preIssueEpochLastBlockNumber);
                // 更新激励池余额
                this.inciteBalance = newInciteBalance;
                // 激励池余额分给区块奖励部分
                BigDecimal blockRewardPart = new BigDecimal(inciteBalance).multiply(chainConfig.getBlockRewardRate());
                this.inciteAmount4Block = blockRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
                // 当前增发周期内每个区块的奖励
                this.blockReward = blockRewardPart.divide(new BigDecimal(chainConfig.getAddIssuePeriodBlockCount()),0,RoundingMode.FLOOR).toBigInteger();
                // 激励池余额分给质押奖励部分
                BigDecimal stakeRewardPart = new BigDecimal(inciteBalance).multiply(chainConfig.getStakeRewardRate());
                this.inciteAmount4Stake = stakeRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
                // 当前增发周期内每个结算周期的质押奖励
                this.settleStakeReward = stakeRewardPart.divide(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue()),0,RoundingMode.FLOOR).toBigInteger();

            } catch (BlockNumberException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
