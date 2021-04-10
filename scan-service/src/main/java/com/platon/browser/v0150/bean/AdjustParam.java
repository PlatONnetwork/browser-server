package com.platon.browser.v0150.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 质押或委托账户调账条目
 */
@Data
public class AdjustParam {
    // 调整时刻的区块号
    private BigInteger currBlockNum;
    // 结算周期区块数
    private BigInteger settleBlockCount;
    // 區塊時間
    private Date blockTime;
    /*特殊节点返回的调账参数*/
    private String optType;
    private String nodeId;
    private String stakingBlockNum;
    private String addr;
    private BigDecimal lock;
    private BigDecimal hes;
    private BigDecimal reward;

    /*委托和质押调整通用属性*/
    private BigDecimal nodeTotalValue; // 节点有效的质押委托总数

    /*委托调整专用属性*/
    private int isHistory; // 委托状态：是否为历史: 1是, 2否
    private BigDecimal delegateHes; // 未锁定委托金额
    private BigDecimal delegateLocked; // 已锁定委托金额
    private BigDecimal delegateReleased; // 待提取的金额
    private BigDecimal nodeStatDelegateValue; // 节点有效的委托金额
    private BigDecimal nodeStatDelegateReleased; // 节点待提取的委托金额
    private BigDecimal nodeHaveDeleReward; // 所有质押已领取委托奖励
    private BigDecimal stakeStatDelegateHes; // 质押未锁定的委托
    private BigDecimal stakeStatDelegateLocked; // 质押锁定的委托
    private BigDecimal stakeStatDelegateReleased; // 质押待提取的委托
    private BigDecimal stakeHaveDeleReward; // 當前质押已领取委托奖励

    /*质押调整专用属性*/
    private int status; // 节点状态:1候选中,2退出中,3已退出,4已锁定
    private int isConsensus; // 是否在共识周期中
    private int isSettle; // 是否在结算周期中
    private BigDecimal stakingHes; // 犹豫期的质押金
    private BigDecimal stakingLocked; // 锁定期的质押金
    private BigDecimal stakingReduction; // 退回中的质押金

    private int stakingReductionEpoch; // 退回结算周期标识
    private int unStakeFreezeDuration; // 解质押理论上锁定的结算周期数
    private Long unStakeEndBlock; // 解质押冻结的最后一个区块
    private Date leaveTime; // 退出時間
}
