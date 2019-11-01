package com.platon.browser.common.collection.dto;

import com.platon.browser.collection.service.epoch.EpochRetryService;
import com.platon.browser.collection.service.epoch.EpochService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前区块相关的所有事件信息(共识周期切换事件/结算周期切换事件/增发周期切换事件)
 */
@Data
@Slf4j
public class EpochMessage {
    private BigInteger currentBlockNumber; // 当前区块号
    private BigInteger consensusEpochRound=BigInteger.ZERO; // 当前所处共识周期轮数
    private BigInteger settleEpochRound=BigInteger.ZERO; // 当前所处结算周期轮数
    private BigInteger issueEpochRound=BigInteger.ZERO; // 当前所处结算周期轮数

    private BigInteger inciteBalance=BigInteger.ZERO; // 当前增发周期开始时的激励池余额 IB
    private BigInteger inciteAmount4Block=BigInteger.ZERO; // 前增发周期开始时的激励池余额分给区块奖励部分 BR=IB*区块奖励比例
    private BigInteger blockReward=BigInteger.ZERO; // 当前增发周期每个区块奖励值 BR/增发周期区块总数
    private BigInteger inciteAmount4Stake=BigInteger.ZERO; // 当前增发周期开始时的激励池余额分给质押奖励部分 SR=IB*质押奖励比例
    private BigInteger settleStakeReward=BigInteger.ZERO;  // 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数
    private BigInteger stakeReward=BigInteger.ZERO; // 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
    private List<Node> curValidators=new ArrayList<>(); // 当前共识周期验证人列表
    private List<Node> curVerifiers=new ArrayList<>(); // 当前结算周期验证人列表

    private EpochMessage(){}
    public static EpochMessage newInstance(){
        return new EpochMessage();
    }
    public EpochMessage updateWithEpochService(EpochService epochService){
        BeanUtils.copyProperties(epochService,this);
        return this;
    }
    public EpochMessage updateWithEpochRetryService(EpochRetryService epochRetryService){
        BeanUtils.copyProperties(epochRetryService,this);
        return this;
    }
}
