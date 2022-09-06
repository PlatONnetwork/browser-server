package com.platon.browser.v0150.context;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.v0150.bean.AdjustParam;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 调账上下文
 */
@Slf4j()
@Data
public abstract class AbstractAdjustContext {

    protected BigInteger blockNumber;

    protected AdjustParam adjustParam;

    protected Staking staking;

    protected Node node;

    protected List<String> errors = new ArrayList<>();

    protected BlockChainConfig chainConfig;

    /**
     * 检查上下文是否有错：需要的数据是否都有
     *
     * @return
     */
    public final List<String> validate() throws BlockNumberException {
        if (chainConfig == null) {
            errors.add("【错误】：BlockChainConfig缺失！");
            return errors;
        }
        if (adjustParam == null) {
            errors.add("【错误】：调账数据缺失！");
            return errors;
        }
        // 调账目标记录是否都存在
        if (node == null) {
            errors.add("【错误】：节点记录缺失:[节点ID=" + adjustParam.getNodeId() + "]");
        }
        if (staking == null) {
            errors.add("【错误】：质押记录缺失:[节点ID=" + adjustParam.getNodeId() + ",节点质押块号=" + adjustParam.getStakingBlockNum() + "]");
        }
        if (node == null || staking == null) {
            return errors;
        }
        // 设置质押相关原始状态和值
        adjustParam.setStatus(staking.getStatus());
        adjustParam.setIsConsensus(staking.getIsConsensus());
        adjustParam.setIsSettle(staking.getIsSettle());
        adjustParam.setStakingHes(staking.getStakingHes());
        adjustParam.setStakingLocked(staking.getStakingLocked());
        adjustParam.setStakingReduction(staking.getStakingReduction());
        adjustParam.setStakingReductionEpoch(staking.getStakingReductionEpoch());
        adjustParam.setStakeHaveDeleReward(staking.getHaveDeleReward());
        // 设置节点相关统计字段原始状态和值
        adjustParam.setNodeTotalValue(node.getTotalValue());
        adjustParam.setNodeStatDelegateValue(node.getStatDelegateValue());
        adjustParam.setNodeStatDelegateReleased(node.getStatDelegateReleased());
        adjustParam.setNodeHaveDeleReward(node.getHaveDeleReward());
        // 设置质押相关统计字段原始状态和值
        adjustParam.setStakeStatDelegateHes(staking.getStatDelegateHes());
        adjustParam.setStakeStatDelegateLocked(staking.getStatDelegateLocked());
        adjustParam.setStakeStatDelegateReleased(staking.getStatDelegateReleased());
        adjustParam.setUnStakeFreezeDuration(staking.getUnStakeFreezeDuration());
        adjustParam.setLeaveTime(staking.getLeaveTime());
        adjustParam.setUnStakeEndBlock(staking.getUnStakeEndBlock());

        // 检查各项金额是否都足够扣减
        validateAmount();
        // 在上下文无错的情况下才能设置调整参数中的委托或质押的状态信息
        if (!errors.isEmpty()) {
            return errors;
        }
        calculateAmountAndStatus();
        return errors;
    }

    /**
     * attention: invoke this method after validate()
     *
     * @return
     */
    public final String contextInfo() {
        StringBuilder sb = new StringBuilder(adjustParam.getOptType()).append("调账参数：\n")
                                                                      .append(JSON.toJSONString(adjustParam))
                                                                      .append("\n")
                                                                      .append("节点记录：\n")
                                                                      .append(JSON.toJSONString(node))
                                                                      .append("\n")
                                                                      .append("质押记录：\n")
                                                                      .append(JSON.toJSONString(staking))
                                                                      .append("\n");
        String extraContextInfo = extraContextInfo();
        if (StringUtils.isNotBlank(extraContextInfo)) {
            sb.append(extraContextInfo).append("\n");
        }
        return sb.toString();
    }

    /**
     * attention: invoke this method after validate()
     * 把错误列表转成错误字符串信息
     *
     * @return
     */
    public final String errorInfo() {
        if (errors.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("============ ");
        if (adjustParam != null) {
            sb.append(adjustParam.getOptType()).append("调账错误 ============\n").append(adjustParam.getOptType()).append("调账参数：\n").append(JSON.toJSONString(adjustParam)).append("\n");
        } else {
            sb.append("调账错误 ============\n");
        }

        if (node != null) {
            sb.append("节点记录：\n").append(JSON.toJSONString(node)).append("\n");
        }
        if (staking != null) {
            sb.append("质押记录：\n").append(JSON.toJSONString(staking)).append("\n");
        }
        String extraContextInfo = extraContextInfo();
        if (StringUtils.isNotBlank(extraContextInfo)) {
            sb.append(extraContextInfo).append("\n");
        }
        errors.forEach(e -> sb.append(e).append("\n"));
        log.error("{}", sb.toString());
        return sb.toString();
    }

    /**
     * 校验调账金额数据
     */
    abstract void validateAmount();

    /**
     * attention: invoke this method after validateAmount()
     * 计算金额及状态，调整参数中与委托或质押相关的状态：
     * 1、如果当前是委托调整上下文，更新委托调整参数的isHistory字段
     * 2、如果当前是质押调整上下文，更新质押调整参数的status、isConsensus、isSettle字段
     */
    abstract void calculateAmountAndStatus() throws BlockNumberException;

    /**
     * attention: invoke this method after validate()
     * 额外的上下文信息
     */
    abstract String extraContextInfo();

}
