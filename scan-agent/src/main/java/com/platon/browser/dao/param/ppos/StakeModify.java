package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;

/**
 * @description: 修改验证人 入库参数
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Data
@Slf4j
@Builder
@Accessors(chain = true)
public class StakeModify implements BusinessParam {
    //节点Id
    private String nodeId;
    //节点名称
    private String nodeName;
    //第三方社交软件关联id
    private String externalId;
    //收益地址
    private String benefitAddr;
    //节点的第三方主页
    private String webSite;
    //节点的描述
    private String details;
    //是否为链初始化时内置的候选人: 1是, 2否
    private int isInit;
    //质押所在区块号
    private BigInteger stakingBlockNum;
    //委托奖励比例
    private Integer nextRewardPer;
    //质押修改时所属结算周期轮数
    private int settleEpoch;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.STAKE_MODIFY;
    }
}
