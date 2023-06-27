package com.platon.browser.v0150.context;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.utils.EpochUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;

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
            adjustParam.getStatus()== Staking.StatusEnum.EXITING.getCode()
            ||adjustParam.getStatus()==Staking.StatusEnum.EXITED.getCode()
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
    void calculateAmountAndStatus() throws BlockNumberException {
        if(
            adjustParam.getStatus()==Staking.StatusEnum.EXITING.getCode()
            ||adjustParam.getStatus()==Staking.StatusEnum.EXITED.getCode()
        ){
            // 退出中或已退出的节点，从stakingReduction中减掉hes和lock
            adjustParam.setStakingReduction(
                adjustParam.getStakingReduction()
                .subtract(adjustParam.getHes())
                .subtract(adjustParam.getLock())
            );
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

            // 质押相关金额扣除金额后，如果(stakingHes+stakingLocked)<质押门槛，则节点状态置为退出中，且锁定周期设置为1，解锁块号设为本周期最后一个块号
            BigDecimal stakingHes = adjustParam.getStakingHes();
            BigDecimal stakingLocked = adjustParam.getStakingLocked();
            if(stakingHes.add(stakingLocked).compareTo(chainConfig.getStakeThreshold())<0){
                adjustParam.setStatus(Staking.StatusEnum.EXITING.getCode());
                adjustParam.setIsConsensus(Staking.YesNoEnum.NO.getCode());
                adjustParam.setIsSettle(Staking.YesNoEnum.NO.getCode());
                // 把锁定期金额移至退回中字段
                adjustParam.setStakingReduction(adjustParam.getStakingReduction().add(stakingLocked));
                // 把犹豫期和锁定期金额置0
                adjustParam.setStakingHes(BigDecimal.ZERO);
                adjustParam.setStakingLocked(BigDecimal.ZERO);
                // 设置退出时所在结算周期
                BigInteger epoch = EpochUtil.getEpoch(adjustParam.getCurrBlockNum(),adjustParam.getSettleBlockCount());
                adjustParam.setStakingReductionEpoch(epoch.intValue());
                //解锁块号=下一结算周期最后一个块号
                BigInteger futureBlockNum = adjustParam.getCurrBlockNum().add(adjustParam.getSettleBlockCount());
                BigInteger unStakeEndBlock = EpochUtil.getCurEpochLastBlockNumber(futureBlockNum,adjustParam.getSettleBlockCount());
                adjustParam.setUnStakeEndBlock(unStakeEndBlock.longValue());
                // 设置退出时间为当前区块时间和冻结周期数为1
                adjustParam.setLeaveTime(adjustParam.getBlockTime());
                adjustParam.setUnStakeFreezeDuration(1);
            }
        }
    }

    @Override
    String extraContextInfo() {
        return null;
    }
}
