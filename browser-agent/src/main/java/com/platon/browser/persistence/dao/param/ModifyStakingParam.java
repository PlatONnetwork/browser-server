//package com.platon.browser.persistence.dao.param;
//
//import lombok.Builder;
//import lombok.Data;
//import lombok.experimental.Accessors;
//
//import java.math.BigInteger;
//import java.util.Date;
//
///**
// * @Auther: dongqile
// * @Date: 2019/10/31
// * @Description:
// */
//@Data
//@Builder
//@Accessors(chain = true)
//public class ModifyStakingParam {
//
//    /**
//     * 节点Id
//     */
//    private String nodeId;
//
//    /**
//     * 节点名称
//     */
//    private String nodeName;
//
//    /**
//     * 第三方社交软件关联id
//     */
//    private String externalId;
//
//    /**
//     * 收益地址
//     */
//    private String benefitAddr;
//
//    /**
//     * 节点的第三方主页
//     */
//    private String webSite;
//
//    /**
//     * 节点的描述
//     */
//    private String details;
//
//    /**
//     * 质押所在区块号
//     */
//    private BigInteger stakingBlockNum;
//
//    /**
//     * 交易所在区块号
//     */
//    private BigInteger bNum;
//
//    /**
//     * 时间
//     */
//    private Date time;
//
//    /**
//     * 是否为链初始化时内置的候选人: 1是, 2否
//     */
//    private int isInit;
//
//    /**
//     * 交易hash
//     */
//    private String txHash;
//
//
//}