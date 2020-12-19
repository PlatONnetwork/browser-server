package com.platon.browser.adjustment.context;

import lombok.Data;

import java.util.List;

/**
 * 质押调账上下文
 */
@Data
public class StakingAdjustContext extends AbstractAdjustContext {
    /**
     * 校验质押相关金额是否满足调账要求
     * @param errors
     */
    @Override
    public void validateAmount(List<String> errors){

        // TODO: 校验质押相关金额是否满足调账要求

        /*
        1、node表：
            total_value【有效的质押委托总数(von)】 - stakingShouldNotHaveAmount
            staking_hes【犹豫期的质押金(von)】 - stakingShouldNotHaveAmount （先减犹豫）
            staking_locked【锁定期的质押金(von)】 - stakingShouldNotHaveAmount' （犹豫不够再减锁定）
            staking_reduction【退回中的质押金(von)】 - stakingShouldNotHaveAmount'' （锁定不够再减待提取）

        2、staking表：
            staking_hes【犹豫期的质押金(von)】 - stakingShouldNotHaveAmount （先减犹豫）
            staking_locked【锁定期的质押金(von)】 - stakingShouldNotHaveAmount' （犹豫不够再减锁定）
            staking_reduction【退回中的质押金(von)】 - stakingShouldNotHaveAmount'' （锁定不够再减待提取）

            金额扣减后，如果 staking_hes+staking_locked < 质押门槛, 则节点执行退出逻辑，状态直接置为已退出。

        3、address表：以下字段由现有的AddressUpdateTask任务定时统计相关表更新，因此不用改动此表
            staking_value 【质押的金额(von)】
            redeemed_value 【赎回中的质押金额(von)】
        */
    }
}
