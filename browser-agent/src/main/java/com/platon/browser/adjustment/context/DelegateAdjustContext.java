package com.platon.browser.adjustment.context;

import com.platon.browser.adjustment.bean.AdjustParam;
import com.platon.browser.dao.entity.Delegation;
import lombok.Data;

import java.util.List;

/**
 * 委托调账上下文
 */
@Data
public class DelegateAdjustContext extends AbstractAdjustContext {
    private Delegation delegation;
    /**
     * 校验委托相关金额是否满足调账要求
     * @param errors
     */
    @Override
    public void validateAmount(List<String> errors){
        AdjustParam diff = getAdjustParam();
        if(delegation==null) errors.add("委托记录缺失:[节点ID="+diff.getNodeId()+",委托人="+diff.getAddr()+",节点质押块号="+diff.getStakingBlockNum()+"]");

        // TODO: 校验委托相关金额是否满足调账要求

        /*
        # 委托调账，对不该有的委托额度 delegationShouldNotHaveAmount，如下表字段需要调整

        1、node表：
            total_value【有效的质押委托总数(von)】 - delegationShouldNotHaveAmount
            stat_delegate_value【有效的委托金额(von)】 - delegationShouldNotHaveAmount （先扣有效委托）
            stat_delegate_released【待提取的委托金额(von)】 - delegationShouldNotHaveAmount （有效委托不够扣再扣待提取）

        2、staking表：
            stat_delegate_hes【未锁定的委托(von)】 - delegationShouldNotHaveAmount  （先减犹豫）
            stat_delegate_locked【锁定的委托(von)】 - delegationShouldNotHaveAmount' （犹豫不够再减锁定）
            stat_delegate_released【待提取的委托(von)】 - delegationShouldNotHaveAmount'' （锁定不够再减待提取）

        3、delegation表：
            delegate_hes【未锁定委托金额(von)】 - delegationShouldNotHaveAmount  （先减犹豫）
            delegate_locked【已锁定委托金额(von)】 - delegationShouldNotHaveAmount'  （犹豫不够再减锁定）
            delegate_released【待提取的金额(von)】 - delegationShouldNotHaveAmount''  （锁定不够再减待提取）

            金额扣减后，如果 delegate_hes+delegate_locked+delegate_released = 0, 则委托变为历史。

        4、address表：以下字段由现有的AddressUpdateTask任务定时统计相关表更新，因此不用改动此表
            delegate_value 【委托的金额(von)】
            delegate_hes【未锁定委托金额(von)】
            delegate_locked【已锁定委托金额(von)】
            delegate_released【待提取的金额(von)】
         */

    }
}
