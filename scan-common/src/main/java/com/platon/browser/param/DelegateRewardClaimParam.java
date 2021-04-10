package com.platon.browser.param;

import com.platon.browser.param.claim.Reward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * User: chendongming
 * Date: 2020/01/02
 * Time: 14:58
 * tyType=5000 领取委托奖励
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class DelegateRewardClaimParam extends TxParam{
    private List<Reward> rewardList;
}
