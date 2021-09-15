package com.platon.browser.dao.param.ppos;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.enums.BusinessType;
import com.platon.browser.param.claim.Reward;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * // TODO: 定义领取奖励业务入库参数
 * @Auther: chendongming
 * @Date: 2020/01/2
 * @Description: 领取奖励
 */
@Data
@Builder
@Accessors(chain = true)
public class DelegateRewardClaim implements BusinessParam {
    // 奖励信息列表
    private List<Reward> rewardList;
    // 领取者地址
    private String address;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.CLAIM_REWARD;
    }
}
