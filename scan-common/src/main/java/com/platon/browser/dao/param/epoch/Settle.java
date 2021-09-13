package com.platon.browser.dao.param.epoch;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * @Auther: dongqile
 * @Date: 2019/11/2
 * @Description: 结算周期切换参数入库
 */
@Data
@Builder
@Accessors(chain = true)
public class Settle implements BusinessParam {
    //当前结算周期验证人
    private Set<String> curVerifierSet;
    //上轮结算周期验证人
    private Set<String> preVerifierSet;
    //质押奖励(von)
    private BigDecimal stakingReward;
    //结算周期
    private int settingEpoch;
    //解除质押锁定金额的轮数
    private int stakingLockEpoch;
    //候选中，退出中，列表
    private List<Staking> stakingList;

    private List<String> exitNodeList;

    @Override
    public BusinessType getBusinessType () {
        return BusinessType.SETTLE_EPOCH;
    }
}