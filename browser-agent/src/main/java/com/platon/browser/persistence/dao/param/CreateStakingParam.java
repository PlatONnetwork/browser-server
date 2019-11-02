package com.platon.browser.persistence.dao.param;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description:
 */
@Data
@Builder
@Accessors(chain = true)
public class CreateStakingParam {
    /**
     * 节点Id
     */
    private String nodeId;

    /**
     * 犹豫期的质押金(von)
     */
    private BigDecimal stakingHes;

    /**
     * 节点名称(质押节点名称)
     */
    private String nodeName;

    /**
     * 第三方社交软件关联id
     */
    private String externalId;

    /**
     * 收益地址
     */
    private String benefitAddr;

    /**
     * 程序版本
     */
    private String programVersion;

    /**
     * 大程序版本
     */
    private String bigVersion;

    /**
     * 节点的第三方主页
     */
    private String webSite;

    /**
     * 节点的描述
     */
    private String details;

    /**
     * 是否为链初始化时内置的候选人: 1是, 2否
     */
    private int isInit;

    /**
     * 质押区块高度
     */
    private BigInteger stakingBlockNum;

    /**
     * 发起质押交易的索引
     */
    private int stakingTxIndex;

    /**
     * 质押地址
     */
    private String stakingAddr;

    /**
     * 加入时间
     */
    private Date joinTime;

    /**
     * 质押交易hash
     */
    private String txHash;

/*    public void init ( String nodeId, BigDecimal stakingHas,String stakingName,
                       String externalId,String benefitAddr,String programVersion,
                       String bigVersion,String webSite,String details,int isInit,
                       BigInteger stakingBlockNum,int stakingTxIndex,String stakingAddr,
                       Date joinTime,String txHash ) {
        this.nodeId = nodeId;
        this.stakingHas = stakingHas;
        this.stakingName = stakingName;
        this.externalId = externalId;
        this.benefitAddr = benefitAddr;
        this.programVersion = programVersion;
        this.bigVersion = bigVersion;
        this.webSite = webSite;
        this.details = details;
        this.isInit = isInit;
        this.stakingBlockNum = stakingBlockNum;
        this.stakingTxIndex  = stakingTxIndex;
        this.stakingAddr = stakingAddr;
        this.joinTime = joinTime;
        this.txHash = txHash;

    }*/
}