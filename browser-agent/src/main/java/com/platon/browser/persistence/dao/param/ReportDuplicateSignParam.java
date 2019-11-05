//package com.platon.browser.persistence.dao.param;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.experimental.Accessors;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.Date;
//
///**
// * @Auther: dongqile
// * @Date: 2019/11/1
// * @Description:
// */
//@Data
//@Builder
//@Accessors(chain = true)
//public class ReportDuplicateSignParam {
//
//    /**
//     * 举报证据
//     */
//    private String slashData;
//
//    /**
//     * 节点Id
//     */
//    private String nodeId;
//
//    /**
//     * 交易hash
//     */
//    private String txHash;
//
//    /**
//     * 交易块高
//     */
//    private BigInteger bNum;
//
//    /**
//     * 时间
//     */
//    private Date time;
//
//    /**
//     * 通过（block_number/每个结算周期出块数）向上取整
//     */
//    private int settingEpoch;
//
//    /**
//     * 质押交易所在块高
//     */
//    private BigInteger stakingBlockNum;
//
//    /**
//     * 双签惩罚比例
//     */
//    private String slashRate;
//
//    /**
//     * 节点状态
//     */
//    private int codeStatus;
//
//    /**
//     * 当前锁定的
//     */
//    private BigDecimal codeCurStakingLocked;
//
//    /**
//     * 奖励的金额
//     */
//    private BigDecimal codeRewardValue;
//
//    /**
//     *  交易发送者
//     */
//    private String benefitAddr;
//
//    /**
//     * 节点操作描述  'PERCENT|AMOUNT' 中
//     */
//    private String codeNodeOptDesc;
//}