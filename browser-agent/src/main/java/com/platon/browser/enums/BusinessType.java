package com.platon.browser.enums;

/**
 * 业务类型枚举
 */
public enum BusinessType {
    // 质押：创建、编辑、增持、退出
    STAKE_CREATE,STAKE_MODIFY,STAKE_INCREASE,STAKE_EXIT,
    // 委托：委托、撤销
    DELEGATE_CREATE,DELEGATE_EXIT,
    // 提案：文本、版本、升级、投票、取消、参数
    PROPOSAL_TEXT,PROPOSAL_VERSION,PROPOSAL_UPGRADE,PROPOSAL_VOTE,PROPOSAL_CANCEL,PROPOSAL_PARAMETER,PROPOSAL_SLASH,
    // 处罚：举报
    REPORT,
    // 版本声明
    VERSION_DECLARE,
    // 创建锁仓计划
    RESTRICTING_CREATE,
    // 新区块
    NEW_BLOCK,
    // 选举、共识周期、结算周期
    ELECTION_EPOCH,CONSENSUS_EPOCH,SETTLE_EPOCH,
    // 网络统计、地址统计
    NETWORK_STATISTIC, ADDRESS_STATISTIC,
    // 领取奖励
    CLAIM_REWARD,
    // 合约创建
    CONTRACT_CREATE,
    // 合约执行
    CONTRACT_EXEC
}
