package com.platon.browser.dao.param.epoch;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

/**
 * @Auther: dongqile
 * @Date: 2019/10/31
 * @Description: 选举周期切换参数入库
 */
@Data
@Builder
@Accessors(chain = true)
public class Election implements BusinessParam {
    /*++++++++++低出块率锁定部分+++++++++++*/
    //需要惩罚的列表
    private List <Staking> lockedNodeList;
    //结算周期
    private int settingEpoch;
    //零出块需要锁定的结算周期数
    private int zeroProduceFreezeDuration;
    //节点被低出块惩罚时所在结算周期
    private int zeroProduceFreezeEpoch;

    /*++++++++++低出块率退出部分+++++++++++*/
    //需要惩罚的列表
    private List <Staking> exitingNodeList;
    //解质押需要经过的结算周期数
    private int unStakeFreezeDuration;
    //解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者
    private BigInteger unStakeEndBlock;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.ELECTION_EPOCH;
    }
}