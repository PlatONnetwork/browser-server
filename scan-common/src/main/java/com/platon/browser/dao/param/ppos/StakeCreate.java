package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @description: 创建质押/创建验证人 入库参数
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeCreate implements BusinessParam {
    //节点Id
    private String nodeId;
    //犹豫期的质押金(von)
    private BigDecimal stakingHes;
    //节点名称(质押节点名称)
    private String nodeName;
    //第三方社交软件关联id
    private String externalId;
    //收益地址
    private String benefitAddr;
    //程序版本
    private String programVersion;
    //大程序版本
    private String bigVersion;
    //节点的第三方主页
    private String webSite;
    //节点的描述
    private String details;
    //是否为链初始化时内置的候选人: 1是, 2否
    private int isInit;
    //质押区块高度
    private BigInteger stakingBlockNum;
    //发起质押交易的索引
    private int stakingTxIndex;
    //质押地址
    private String stakingAddr;
    //加入时间
    private Date joinTime;
    //质押交易hash
    private String txHash;
    //委托奖励比例
    private int delegateRewardPer;
    //解质押需要经过的结算周期数
    private int unStakeFreezeDuration;
    //解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者
    private BigInteger unStakeEndBlock;
    //质押创建时所属结算周期轮数
    private int settleEpoch;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_CREATE;
    }
}
