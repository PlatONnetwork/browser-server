package com.platon.browser.bean;


import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.browser.service.epoch.EpochRetryService;
import com.platon.browser.service.epoch.EpochService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前区块相关的所有事件信息(共识周期切换事件/结算周期切换事件/增发周期切换事件)
 */
@Data
@Slf4j
public class EpochMessage {

    /**
     * 当前区块号
     */
    private BigInteger currentBlockNumber;

    /**
     * 当前所处共识周期轮数
     */
    private BigInteger consensusEpochRound = BigInteger.ZERO;

    /**
     * 当前所处结算周期轮数
     */
    private BigInteger settleEpochRound = BigInteger.ZERO;

    /**
     * 当前所处增发周期轮数
     */
    private BigInteger issueEpochRound = BigInteger.ZERO;

    /**
     * 当前增发周期每个区块奖励值 BR/增发周期区块总数
     */
    private BigDecimal blockReward = BigDecimal.ZERO;

    /**
     * 当前增发周期的每个结算周期质押奖励值 SSR=SR/一个增发周期包含的结算周期数
     */
    private BigDecimal settleStakeReward = BigDecimal.ZERO;

    /**
     * 当前结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
     */
    private BigDecimal stakeReward = BigDecimal.ZERO;

    /**
     * 前一结算周期每个节点的质押奖励值 PerNodeSR=SSR/当前结算周期实际验证人数
     */
    private BigDecimal preStakeReward = BigDecimal.ZERO;

    /**
     * 前一共识周期验证人列表
     */
    private List<Node> preValidatorList = new ArrayList<>();

    /**
     * 当前共识周期验证人列表
     */
    private List<Node> curValidatorList = new ArrayList<>();

    /**
     * 前一结算周期验证人列表
     */
    private List<Node> preVerifierList = new ArrayList<>();

    /**
     * 当前结算周期验证人列表
     */
    private List<Node> curVerifierList = new ArrayList<>();

    /**
     * 当前期望出块数
     */
    private Long expectBlockCount = 0L;

    private EpochMessage() {
    }

    public static EpochMessage newInstance() {
        return new EpochMessage();
    }

    public EpochMessage updateWithEpochService(EpochService epochService) {
        BeanUtils.copyProperties(epochService, this);
        return this;
    }

    public EpochMessage updateWithEpochRetryService(EpochRetryService epochRetryService) {
        BeanUtils.copyProperties(epochRetryService, this);
        preValidatorList.addAll(epochRetryService.getPreValidators());
        curValidatorList.addAll(epochRetryService.getCurValidators());
        preVerifierList.addAll(epochRetryService.getPreVerifiers());
        curVerifierList.addAll(epochRetryService.getCurVerifiers());
        return this;
    }

}
