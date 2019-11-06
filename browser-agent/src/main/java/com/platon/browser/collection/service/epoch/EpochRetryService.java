package com.platon.browser.collection.service.epoch;

import com.platon.browser.client.PlatOnClient;
import com.platon.browser.client.SpecialContractApi;
import com.platon.browser.collection.exception.CandidateException;
import com.platon.browser.common.service.AccountService;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.utils.EpochUtil;
import com.platon.browser.utils.HexTool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 奖励计算服务
 * 1、根据区块号计算周期切换相关值：
 *      名称/含义                                                                   变量名称
 *    当前增发周期开始时的激励池余额 IB                                             inciteBalance
 *    当前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例               inciteAmount4Block
 *    当前增发周期每个区块奖励值 BR/增发周期区块总数                                 blockReward
 *    当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例               inciteAmount4Stake
 *    当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数        settleStakeReward
 *    当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数         stakeReward
 *    当前共识周期验证人                                                            curValidators
 *    当前结算周期验证人                                                            curVerifiers
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

    @Getter private BigInteger inciteBalance=BigInteger.ZERO; // 当前增发周期开始时的激励池余额 IB
    @Getter private BigInteger inciteAmount4Block=BigInteger.ZERO; // 前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例
    @Getter private BigInteger blockReward=BigInteger.ZERO; // 当前增发周期每个区块奖励值 BR/增发周期区块总数
    @Getter private BigInteger inciteAmount4Stake=BigInteger.ZERO; // 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例
    @Getter private BigInteger settleStakeReward=BigInteger.ZERO;  // 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数
    @Getter private BigInteger stakeReward=BigInteger.ZERO; // 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
    @Getter private List<Node> preValidators=new ArrayList<>(); // 前一共识周期验证人列表
    @Getter private List<Node> curValidators=new ArrayList<>(); // 当前共识周期验证人列表
    @Getter private List<Node> preVerifiers=new ArrayList<>(); // 前一结算周期验证人列表
    @Getter private List<Node> curVerifiers=new ArrayList<>(); // 当前结算周期验证人列表
    @Getter private Long expectBlockCount=0L; // 当前期望出块数

    /**
     * 增发周期变更:
     * 必然伴随着结算周期和共识周期的变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void issueChange(BigInteger currentBlockNumber) throws Exception {
        try {
            // >>>>如果增发周期变更,则更新相应的奖励字段
            // >>>>当前增发周期的初始激励池余额需要在上一增发周期最后一个块时候确定
            // 上一增发周期最后一个块号
            BigInteger preIssueEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getAddIssuePeriodBlockCount());
            // 当前增发周期开始时的激励池余额
            inciteBalance = accountService.getInciteBalance(preIssueEpochLastBlockNumber);
            // 激励池余额分给区块奖励部分
            BigDecimal blockRewardPart = new BigDecimal(inciteBalance).multiply(chainConfig.getBlockRewardRate());
            inciteAmount4Block = blockRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
            // 当前增发周期内每个区块的奖励
            blockReward = blockRewardPart.divide(new BigDecimal(chainConfig.getAddIssuePeriodBlockCount()),0,RoundingMode.FLOOR).toBigInteger();
            // 激励池余额分给质押奖励部分
            BigDecimal stakeRewardPart = new BigDecimal(inciteBalance).multiply(chainConfig.getStakeRewardRate());
            inciteAmount4Stake = stakeRewardPart.setScale(0,RoundingMode.FLOOR).toBigInteger();
            // 当前增发周期内每个结算周期的质押奖励
            settleStakeReward = stakeRewardPart.divide(new BigDecimal(chainConfig.getSettlePeriodCountPerIssue()),0,RoundingMode.FLOOR).toBigInteger();
            // 触发共识周期变更
            consensusChange(currentBlockNumber);
            // 触发结算周期变更
            settlementChange(currentBlockNumber);
            // 计算当前结算周期内每个验证人的质押奖励
            stakeReward = new BigDecimal(settleStakeReward).divide(BigDecimal.valueOf(curVerifiers.size()),0,RoundingMode.FLOOR).toBigInteger();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }

    /**
     * 共识周期变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void consensusChange(BigInteger currentBlockNumber) throws Exception {
        try {
            // 当前块所处的共识周期
            BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());
            // 链上最新块所处的共识周期
            Web3j web3j = platOnClient.getWeb3j();
            BigInteger latestBlockNumber = platOnClient.getLatestBlockNumber();
            BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber,chainConfig.getConsensusPeriodBlockCount());
            // 上一个周期的最后一个块号
            BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getConsensusPeriodBlockCount());

            // 前一周期的验证人
            List<Node> preNodes = specialContractApi.getHistoryValidatorList(web3j,preEpochLastBlockNumber);
            preNodes.forEach(n->n.setNodeId(HexTool.prefix(n.getNodeId())));
            preValidators.clear();
            preValidators.addAll(preNodes);

            // 当前周期的验证人
            List<Node> curNodes= Collections.emptyList();
            if(latestEpoch.compareTo(currentEpoch)>0){
                // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
                // 当前周期的第一个块号
                BigInteger curEpochFirstBlockNumber = preEpochLastBlockNumber.add(BigInteger.ONE);
                curNodes = specialContractApi.getHistoryValidatorList(web3j,curEpochFirstBlockNumber);
            }
            if(latestEpoch.compareTo(currentEpoch)==0){
                // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
                curNodes = platOnClient.getLatestValidators();
            }
            curNodes.forEach(n->n.setNodeId(HexTool.prefix(n.getNodeId())));
            curValidators.clear();
            curValidators.addAll(curNodes);

            // 更新期望出块数：期望出块数=共识周期块数/实际参与共识节点数
            expectBlockCount=chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(curValidators.size())).longValue();
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }

    /**
     * 结算周期变更
     * @param currentBlockNumber 当前区块号
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public void settlementChange(BigInteger currentBlockNumber) throws Exception {
        try {
            // 当前块所处周期
            BigInteger currentEpoch = EpochUtil.getEpoch(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());
            // 链上最新块所处周期
            Web3j web3j = platOnClient.getWeb3j();
            BigInteger latestBlockNumber = platOnClient.getLatestBlockNumber();
            BigInteger latestEpoch = EpochUtil.getEpoch(latestBlockNumber,chainConfig.getSettlePeriodBlockCount());
            // 上一个周期的最后一个块号
            BigInteger preEpochLastBlockNumber = EpochUtil.getPreEpochLastBlockNumber(currentBlockNumber,chainConfig.getSettlePeriodBlockCount());

            // 前一周期的验证人
            List<Node> preNodes = specialContractApi.getHistoryVerifierList(web3j,preEpochLastBlockNumber);
            preNodes.forEach(n->n.setNodeId(HexTool.prefix(n.getNodeId())));
            preVerifiers.clear();
            preVerifiers.addAll(preNodes);

            // 当前周期的验证人
            List<Node> curNodes = Collections.emptyList();
            if(latestEpoch.compareTo(currentEpoch)>0){
                // >>>>如果链上最新块所在周期>当前块所处周期, 则查询特殊节点历史接口
                // 当前周期的第一个块号
                BigInteger curEpochFirstBlockNumber = preEpochLastBlockNumber.add(BigInteger.ONE);
                curNodes = specialContractApi.getHistoryVerifierList(web3j,curEpochFirstBlockNumber);
            }
            if(latestEpoch.compareTo(currentEpoch)==0){
                // >>>>如果链上最新块所在周期==当前块所处周期, 则查询实时接口
                curNodes = platOnClient.getLatestVerifiers();
            }
            curNodes.forEach(n->n.setNodeId(HexTool.prefix(n.getNodeId())));
            curVerifiers.clear();
            curVerifiers.addAll(curNodes);
        }catch (Exception e){
            log.error("",e);
            throw e;
        }
    }


    /**
     * 获取实时候选人列表
     * @return
     * @throws Exception
     */
    @Retryable(value = Exception.class, maxAttempts = Integer.MAX_VALUE)
    public List<Node> getCandidates() throws Exception {
        try {
            BaseResponse<List<Node>> br = platOnClient.getNodeContract().getCandidateList().send();
            if (!br.isStatusOk()) throw new CandidateException(br.errMsg);
            List<Node> candidates = br.data;
            if(candidates==null) throw new CandidateException("实时候选节点列表为空!");
            candidates.forEach(v->v.setNodeId(HexTool.prefix(v.getNodeId())));
            return candidates;
        } catch (Exception e) {
            log.error("",e);
            throw e;
        }
    }
}
