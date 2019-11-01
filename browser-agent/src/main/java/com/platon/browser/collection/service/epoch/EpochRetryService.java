package com.platon.browser.collection.service.epoch;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.common.service.AccountService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.utils.EpochUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 奖励计算服务
 */
@Slf4j
@Service
public class EpochRetryService {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PlatOnClient platOnClient;
    @Autowired
    private SpecialContractApi specialContractApi;

    /**
     * 周期切换上下文件
     */
    @Data
    class EpochContext{
        private BigInteger inciteBalance=BigInteger.ZERO; // 当前增发周期开始时的激励池余额 IB
        private BigInteger inciteAmount4Block=BigInteger.ZERO; // 前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例
        private BigInteger blockReward=BigInteger.ZERO; // 当前增发周期每个区块奖励值 BR/增发周期区块总数
        private BigInteger inciteAmount4Stake=BigInteger.ZERO; // 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例
        private BigInteger settleStakeReward=BigInteger.ZERO;  // 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数
        private BigInteger stakeReward=BigInteger.ZERO; // 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
        private List<Node> curValidators=new ArrayList<>(); // 当前共识周期验证人列表
        private List<Node> preValidators=new ArrayList<>(); // 前一共识周期验证人列表
        private List<Node> curVerifiers=new ArrayList<>(); // 当前结算周期验证人列表
        private List<Node> preVerifiers=new ArrayList<>(); // 前一结算周期验证人列表
        public void reset(){
            curValidators.clear();
            curVerifiers.clear();
            preValidators.clear();
            preVerifiers.clear();
        }
    }

    private EpochContext ec = new EpochContext();

    /**
     * 增发周期变更:
     * 必然伴随着结算周期和共识周期的变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public EpochContext issueEpochChange(BigInteger currentBlockNumber) throws Exception {
        // >>>>如果增发周期变更,则更新相应的奖励字段
        // >>>>当前增发周期的初始激励池余额需要在上一增发周期最后一个块时候确定
        // 上一增发周期最后一个块号
        BigInteger preIssueEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getAddIssuePeriodBlockCount());
        // 当前增发周期开始时的激励池余额
        ec.inciteBalance = accountService.getInciteBalance(preIssueEpochLastBlockNumber);
        // 激励池余额分给区块奖励部分
        BigDecimal blockRewardPart = new BigDecimal(ec.inciteBalance).multiply(chainConfig.getBlockRewardRate());
        ec.inciteAmount4Block = blockRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
        // 当前增发周期内每个区块的奖励
        ec.blockReward = blockRewardPart.divide(new BigDecimal(chainConfig.getAddIssuePeriodBlockCount()),0,RoundingMode.FLOOR).toBigInteger();
        // 激励池余额分给质押奖励部分
        BigDecimal stakeRewardPart = new BigDecimal(ec.inciteBalance).multiply(chainConfig.getStakeRewardRate());
        ec.inciteAmount4Stake = stakeRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
        // 当前增发周期内每个结算周期的质押奖励
        ec.settleStakeReward = stakeRewardPart.divide(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue()),0,RoundingMode.FLOOR).toBigInteger();
        // 重置验证人
        ec.reset();
        // 触发共识周期变更
        consensusEpochChange(currentBlockNumber);
        // 触发结算周期变更
        settlementEpochChange(currentBlockNumber);
        // 计算当前结算周期内每个验证人的质押奖励
        ec.stakeReward = new BigDecimal(ec.settleStakeReward).divide(BigDecimal.valueOf(ec.getCurVerifiers().size()),0,RoundingMode.FLOOR).toBigInteger();
        return ec;
    }

    /**
     * 共识周期变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void consensusEpochChange(BigInteger currentBlockNumber) throws Exception {
        // 当前块所处的共识周期
        BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());
        // 链上最新块所处的共识周期
        Web3j web3j = platOnClient.getWeb3j();
        BigInteger latestBlockNumber = getLatestBlockNumber();
        BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber,chainConfig.getConsensusPeriodBlockCount());
        // 上一个共识周期的最后一个块号
        BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());
        List<Node> preNodes = specialContractApi.getHistoryValidatorList(web3j,preEpochLastBlockNumber);
        ec.getPreValidators().addAll(preNodes);
        if(latestEpoch.compareTo(currentEpoch)>0){
            // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
            // 当前共识周期的第一个块号
            BigInteger curEpochFirstBlockNumber = preEpochLastBlockNumber.add(BigInteger.ONE);
            List<Node> curNodes = specialContractApi.getHistoryValidatorList(web3j,curEpochFirstBlockNumber);
            ec.getCurValidators().addAll(curNodes);
        }
        if(latestEpoch.compareTo(currentEpoch)==0){
            // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
            List<Node> curNodes = platOnClient.getNodeContract().getValidatorList().send().data;
            ec.getCurValidators().addAll(curNodes);
        }
    }

    /**
     * 结算周期变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void settlementEpochChange(BigInteger currentBlockNumber) throws Exception {
        // 当前块所处的结算周期
        BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());
        // 链上最新块所处的共识周期
        Web3j web3j = platOnClient.getWeb3j();
        BigInteger latestBlockNumber = getLatestBlockNumber();
        BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber,chainConfig.getSettlePeriodBlockCount());

        // 上一个共识周期的最后一个块号
        BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());
        List<Node> preNodes = specialContractApi.getHistoryVerifierList(web3j,preEpochLastBlockNumber);
        ec.getPreVerifiers().addAll(preNodes);
        if(latestEpoch.compareTo(currentEpoch)>0){
            // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
            // 当前共识周期的第一个块号
            BigInteger curEpochFirstBlockNumber = preEpochLastBlockNumber.add(BigInteger.ONE);
            List<Node> curNodes = specialContractApi.getHistoryVerifierList(web3j,curEpochFirstBlockNumber);
            ec.getCurVerifiers().addAll(curNodes);
        }
        if(latestEpoch.compareTo(currentEpoch)==0){
            // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
            List<Node> curNodes = platOnClient.getNodeContract().getVerifierList().send().data;
            ec.getCurVerifiers().addAll(curNodes);
        }
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return platOnClient.getWeb3j().platonBlockNumber().send().getBlockNumber();
    }
}
