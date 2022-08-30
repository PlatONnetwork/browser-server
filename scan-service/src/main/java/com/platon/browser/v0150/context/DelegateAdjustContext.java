package com.platon.browser.v0150.context;

import com.alibaba.fastjson.JSON;
import com.platon.browser.bean.CustomDelegation;
import com.platon.browser.bean.CustomStaking;
import com.platon.browser.dao.entity.Delegation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 委托调账上下文
 * 必须数据：
 * 1、质押信息
 * 2、节点信息
 * 3、委托信息
 */
@Slf4j
@Data
public class DelegateAdjustContext extends AbstractAdjustContext {

    private Delegation delegation;

    /**
     * 校验委托相关金额是否满足调账要求
     */
    @Override
    void validateAmount() {
        if (delegation == null) {
            errors.add("【错误】：委托记录缺失:[节点ID=" + adjustParam.getNodeId() + ",节点质押块号=" + adjustParam.getStakingBlockNum() + ",委托人=" + adjustParam.getAddr() + "]");
            return;
        }

        // 设置委托原始状态
        adjustParam.setIsHistory(delegation.getIsHistory());
        // 设置委托原始金额
        adjustParam.setDelegateHes(delegation.getDelegateHes());
        adjustParam.setDelegateLocked(delegation.getDelegateLocked());
        adjustParam.setDelegateReleased(delegation.getDelegateReleased());
        // 设置节点原始的委托汇总金额
        adjustParam.setNodeStatDelegateValue(node.getStatDelegateValue());
        adjustParam.setNodeStatDelegateReleased(node.getStatDelegateReleased());
        // 设置质押原始的委托汇总金额
        adjustParam.setStakeStatDelegateHes(staking.getStatDelegateHes());
        adjustParam.setStakeStatDelegateLocked(staking.getStatDelegateLocked());
        adjustParam.setStakeStatDelegateReleased(staking.getStatDelegateReleased());
        if (adjustParam.getStatus() == CustomStaking.StatusEnum.EXITING.getCode() || adjustParam.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
            // 退出中或已退出的节点，从委托的delegateReleased中减掉hes和lock
            if (adjustParam.getDelegateReleased().compareTo(adjustParam.getHes().add(adjustParam.getLock())) < 0) {
                errors.add("【错误】：委托记录[待提取金额【" + adjustParam.getDelegateReleased() + "】]小于调账参数[犹豫期【" + adjustParam.getHes() + "】+锁定期【" + adjustParam.getLock() + "】]金额【" + adjustParam.getHes()
                                                                                                                                                                                     .add(adjustParam.getLock()) + "】！");
            }
        } else {
            // 候选中或已锁定的节点，从委托各自的犹豫期或锁定期金额中扣除
            if (adjustParam.getDelegateHes().compareTo(adjustParam.getHes()) < 0) {
                errors.add("【错误】：委托记录[犹豫期金额【" + adjustParam.getDelegateHes() + "】]小于调账参数[犹豫期金额【" + adjustParam.getHes() + "】]！");
            }
            if (adjustParam.getDelegateLocked().compareTo(adjustParam.getLock()) < 0) {
                errors.add("【错误】：委托记录[锁定期金额【" + adjustParam.getDelegateLocked() + "】]小于调账参数[锁定期金额【" + adjustParam.getLock() + "】]！");
            }
        }
    }

    @Override
    void calculateAmountAndStatus() {
        if (adjustParam.getStatus() == CustomStaking.StatusEnum.EXITING.getCode() || adjustParam.getStatus() == CustomStaking.StatusEnum.EXITED.getCode()) {
            // 退出中或已退出的节点
            // 1、从委托的delegateReleased中减掉hes和lock
            adjustParam.setDelegateReleased(adjustParam.getDelegateReleased().subtract(adjustParam.getHes()).subtract(adjustParam.getLock()));
            // 2、从节点的statDelegateReleased减掉lock
            adjustParam.setNodeStatDelegateReleased(adjustParam.getNodeStatDelegateReleased().subtract(adjustParam.getHes()).subtract(adjustParam.getLock()));
            // 3、从质押的statDelegateReleased减掉hes和lock
            adjustParam.setStakeStatDelegateReleased(adjustParam.getStakeStatDelegateReleased().subtract(adjustParam.getHes()).subtract(adjustParam.getLock()));
        } else {
            // 候选中或已锁定的节点
            // 1、从委托的犹豫期或锁定期金额中扣除
            adjustParam.setDelegateHes(adjustParam.getDelegateHes().subtract(adjustParam.getHes()));
            adjustParam.setDelegateLocked(adjustParam.getDelegateLocked().subtract(adjustParam.getLock()));
            // 2、从节点的statDelegateValue减掉hes和lock
            adjustParam.setNodeStatDelegateValue(adjustParam.getNodeStatDelegateValue().subtract(adjustParam.getHes()).subtract(adjustParam.getLock()));
            // 3、从质押的statDelegateHes减掉hes
            adjustParam.setStakeStatDelegateHes(adjustParam.getStakeStatDelegateHes().subtract(adjustParam.getHes()));
            // 4、从质押的statDelegateLocked减掉lock
            adjustParam.setStakeStatDelegateLocked(adjustParam.getStakeStatDelegateLocked().subtract(adjustParam.getLock()));
            // 5、从节点的总的有效质押和委托totalValue中减掉hes和lock
            adjustParam.setNodeTotalValue(adjustParam.getNodeTotalValue().subtract(adjustParam.getHes()).subtract(adjustParam.getLock()));
        }
        // 质押相关金额扣除金额后，如果(delegateHes+delegateLocked+delegateReleased)==0，则委托置为历史
        BigDecimal delegateHes = adjustParam.getDelegateHes();
        BigDecimal delegateLocked = adjustParam.getDelegateLocked();
        BigDecimal delegateReleased = adjustParam.getDelegateReleased();
        if (delegateHes.add(delegateLocked).add(delegateReleased).compareTo(BigDecimal.ZERO) <= 0) {
            adjustParam.setIsHistory(CustomDelegation.YesNoEnum.YES.getCode());

            // 节点和质押中加上调账参数中的reward
            adjustParam.setStakeHaveDeleReward(adjustParam.getStakeHaveDeleReward().add(adjustParam.getReward()));
            adjustParam.setNodeHaveDeleReward(adjustParam.getNodeHaveDeleReward().add(adjustParam.getReward()));
        }
    }

    @Override
    String extraContextInfo() {
        if (delegation == null) {
            return "";
        }
        String extra = "委托记录：\n" + JSON.toJSONString(delegation);
        return extra;
    }

}
