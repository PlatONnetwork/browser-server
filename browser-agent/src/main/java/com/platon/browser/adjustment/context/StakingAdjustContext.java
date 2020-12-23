package com.platon.browser.adjustment.context;

import com.platon.browser.dto.CustomStaking;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 质押调账上下文
 * 必须数据：
 * 1、质押信息
 * 2、节点信息
 */
@Slf4j
@Data
public class StakingAdjustContext extends AbstractAdjustContext {
    /**
     * 校验质押相关金额是否满足调账要求
     */
    @Override
    void validateAmount(){
        // 验证金额是否正确前，检查各项必须的数据是否存在
        if (!errors.isEmpty()) return;
        if(
            adjustParam.getStatus()==CustomStaking.StatusEnum.EXITING.getCode()
            ||adjustParam.getStatus()==CustomStaking.StatusEnum.EXITED.getCode()
        ){
            // 退出中或已退出的节点，判断stakingReduction金额是否够扣
            if(adjustParam.getStakingReduction().compareTo(adjustParam.getHes().add(adjustParam.getLock()))<0){
                errors.add("【错误】：质押记录[退回中金额【"+adjustParam.getStakingReduction()+"】]小于调账参数[犹豫期【"+adjustParam.getHes()+"】+锁定期【"+adjustParam.getLock()+"】]金额【"+adjustParam.getHes().add(adjustParam.getLock())+"】！");
            }
        }else{
            // 候选中或已锁定的节点，判断犹豫期和锁定期金额是否够扣
            if(adjustParam.getStakingHes().compareTo(adjustParam.getHes())<0){
                errors.add("【错误】：质押记录[犹豫期金额【"+adjustParam.getStakingHes()+"】]小于调账参数[犹豫期金额【"+adjustParam.getHes()+"】]！");
            }
            if(adjustParam.getStakingLocked().compareTo(adjustParam.getLock())<0){
                errors.add("【错误】：质押记录[锁定期金额【"+adjustParam.getStakingLocked()+"】]小于调账参数[锁定期金额【"+adjustParam.getLock()+"】]！");
            }
        }
    }

    @Override
    void calculateAmountAndStatus() {
        if(
            adjustParam.getStatus()==CustomStaking.StatusEnum.EXITING.getCode()
            ||adjustParam.getStatus()==CustomStaking.StatusEnum.EXITED.getCode()
        ){
            // 退出中或已退出的节点，从stakingReduction中减掉hes和lock
            adjustParam.getStakingReduction().subtract(adjustParam.getHes()).subtract(adjustParam.getLock());
        }else{
            // 候选中或已锁定的节点，从各自的犹豫期或锁定期金额中扣除
            adjustParam.setStakingHes(adjustParam.getStakingHes().subtract(adjustParam.getHes()));
            adjustParam.setStakingLocked(adjustParam.getStakingLocked().subtract(adjustParam.getLock()));
            // 从节点的总的有效质押和委托totalValue中减掉hes和lock
            adjustParam.setNodeTotalValue(
                adjustParam.getNodeTotalValue()
                .subtract(adjustParam.getHes())
                .subtract(adjustParam.getLock())
            );
        }
        // 质押相关金额扣除金额后，如果(stakingHes+stakingLocked)<质押门槛，则节点状态置为已退出
        BigDecimal stakingHes = adjustParam.getStakingHes();
        BigDecimal stakingLocked = adjustParam.getStakingLocked();
        if(stakingHes.add(stakingLocked).compareTo(chainConfig.getStakeThreshold())<0){
            adjustParam.setStatus(CustomStaking.StatusEnum.EXITED.getCode());
            adjustParam.setIsConsensus(CustomStaking.YesNoEnum.NO.getCode());
            adjustParam.setIsSettle(CustomStaking.YesNoEnum.NO.getCode());
        }
    }

    @Override
    String extraContextInfo() {
        return null;
    }
}
