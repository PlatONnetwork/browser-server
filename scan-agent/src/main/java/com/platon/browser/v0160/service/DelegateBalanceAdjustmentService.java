package com.platon.browser.v0160.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.platon.browser.bean.RecoveredDelegationAmount;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.CustomAddressMapper;
import com.platon.browser.dao.mapper.CustomDelegationMapper;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.v0160.bean.FixIssue1583;
import com.platon.browser.v0160.bean.RecoveredDelegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;

/**
 * 兼容底层升级到0.16.0的调账功能，对应底层issue1583
 */
@Slf4j
@Service
public class DelegateBalanceAdjustmentService {

    @Resource
    private NodeMapper nodeMapper;
    @Resource
    private StakingMapper stakingMapper;
    @Resource
    private CustomDelegationMapper customDelegationMapper;
    @Resource
    private CustomAddressMapper customAddressMapper;

    /**
     * 调账
     *
     * @return:
     * @date: 2021/6/25
     */
    public void adjust() throws Exception {
        List<FixIssue1583> list = FixIssue1583.getRecoveredDelegationInfo();
        List<RecoveredDelegationAmount> recoveredDelegationAmountList = CollUtil.newArrayList();
        for (FixIssue1583 node : list) {
            // 总共被领取的委托奖励
            BigInteger totalDeleReward = BigInteger.valueOf(0);
            // 总委托
            BigInteger totalDelegationAmount = node.getTotalDelegationAmount();
            // 总委托奖励
            BigInteger totalDelegationRewardAmount = node.getTotalDelegationRewardAmount();
            for (RecoveredDelegation recoveredDelegation : node.getRecoveredDelegationList()) {
                // 计算用户可以找回的委托奖励(算法和底层保持一致，注意：要保证和底层结果一样)
                BigInteger recoveredDelegationAmount = recoveredDelegation.getDelegationAmount().multiply(totalDelegationRewardAmount).divide(totalDelegationAmount);
                totalDeleReward = totalDeleReward.add(recoveredDelegationAmount);
                RecoveredDelegationAmount recovered = new RecoveredDelegationAmount();
                recovered.setDelegateAddr(recoveredDelegation.getAddress());
                recovered.setStakingBlockNum(node.getStakingBlockNumber());
                recovered.setNodeId(node.getNodeId());
                recovered.setRecoveredDelegationAmount(new BigDecimal(recoveredDelegationAmount));
                recoveredDelegationAmountList.add(recovered);
            }
            updateNode(node.getNodeId(), totalDeleReward);
            StakingKey stakingKey = new Staking();
            stakingKey.setNodeId(node.getNodeId());
            stakingKey.setStakingBlockNum(node.getStakingBlockNumber());
            updateStaking(stakingKey, totalDeleReward);
        }
        if (CollUtil.isNotEmpty(recoveredDelegationAmountList)) {
            updateAddressAndDelegation(recoveredDelegationAmountList);
        }
    }

    /**
     * 更新node表
     *
     * @param nodeId          节点id
     * @param totalDeleReward 总共被领取的委托奖励
     * @return: void
     * @date: 2021/6/25
     */
    private void updateNode(String nodeId, BigInteger totalDeleReward) throws Exception {
        Node nodeInfo = nodeMapper.selectByPrimaryKey(nodeId);
        if (ObjectUtil.isNull(nodeInfo)) {
            throw new Exception(StrUtil.format("找不到对应节点信息,nodeId:[{}]", nodeId));
        } else {
            Node newNode = new Node();
            newNode.setNodeId(nodeInfo.getNodeId());
            // stat_delegate_released 待提取的委托金额
            BigDecimal oldStatDelegateReleased = nodeInfo.getStatDelegateReleased();
            BigDecimal newStatDelegateReleased = oldStatDelegateReleased.subtract(new BigDecimal(totalDeleReward));
            newNode.setStatDelegateReleased(newStatDelegateReleased);
            // have_dele_reward 所有质押已领取委托奖励
            BigDecimal oldHaveDeleReward = nodeInfo.getHaveDeleReward();
            BigDecimal newHaveDeleReward = oldHaveDeleReward.add(new BigDecimal(totalDeleReward));
            newNode.setHaveDeleReward(newHaveDeleReward);
            int res = nodeMapper.updateByPrimaryKeySelective(newNode);
            if (res > 0) {
                log.info("issue1583调账：更新node表成功,nodeId:[{}]：待提取的委托金额新值[{}]=待提取的委托金额旧值[{}]-总共被领取的委托奖励[{}];" +
                                "所有质押已领取委托奖励新值[{}]=所有质押已领取委托奖励旧值[{}]+总共被领取的委托奖励[{}];",
                        nodeInfo.getNodeId(),
                        newStatDelegateReleased, oldStatDelegateReleased, totalDeleReward,
                        newHaveDeleReward, oldHaveDeleReward, totalDeleReward);
            } else {
                log.error("issue1583调账：更新node表失败,nodeId:[{}]", nodeInfo.getNodeId());
            }
        }
    }

    /**
     * 更新staking表
     *
     * @param stakingKey      质押key
     * @param totalDeleReward 总共被领取的委托奖励
     * @return: void
     * @date: 2021/6/25
     */
    private void updateStaking(StakingKey stakingKey, BigInteger totalDeleReward) throws Exception {
        Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
        if (ObjectUtil.isNull(staking)) {
            throw new Exception(StrUtil.format("找不到对应的质押信息,stakingKey:[{}]", JSONUtil.toJsonStr(stakingKey)));
        } else {
            Staking newStaking = new Staking();
            newStaking.setNodeId(staking.getNodeId());
            newStaking.setStakingBlockNum(staking.getStakingBlockNum());
            // stat_delegate_released 待提取的委托
            BigDecimal oldStatDelegateReleased = staking.getStatDelegateReleased();
            BigDecimal newStatDelegateReleased = oldStatDelegateReleased.subtract(new BigDecimal(totalDeleReward));
            newStaking.setStatDelegateReleased(newStatDelegateReleased);
            // have_dele_reward 节点当前质押已领取委托奖励
            BigDecimal oldHaveDeleReward = staking.getHaveDeleReward();
            BigDecimal newHaveDeleReward = staking.getHaveDeleReward().add(new BigDecimal(totalDeleReward));
            newStaking.setHaveDeleReward(newHaveDeleReward);
            int res = stakingMapper.updateByPrimaryKeySelective(newStaking);
            if (res > 0) {
                log.info("issue1583调账：更新staking表成功,stakingKey:[{}]：待提取的委托新值[{}]=待提取的委托旧值[{}]-总共被领取的委托奖励[{}];" +
                                "节点当前质押已领取委托奖励新值[{}]=节点当前质押已领取委托奖励旧值[{}]+总共被领取的委托奖励[{}];",
                        JSONUtil.toJsonStr(stakingKey),
                        newStatDelegateReleased, oldStatDelegateReleased, totalDeleReward,
                        newHaveDeleReward, oldHaveDeleReward, totalDeleReward);
            } else {
                log.error("issue1583调账：更新staking表失败,,stakingKey:[{}]", JSONUtil.toJsonStr(stakingKey));
            }
        }
    }

    /**
     * 更新delegation表和address表
     *
     * @param list
     * @return: void
     * @date: 2021/6/29
     */
    private void updateAddressAndDelegation(List<RecoveredDelegationAmount> list) {
        try {
            customDelegationMapper.batchUpdateByDelegationKey(list);
            customAddressMapper.batchUpdateByAddress(list);
            log.info("issue1583调账：更新delegation表和address表成功,数据为:[{}]", JSONUtil.toJsonStr(list));
        } catch (Exception e) {
            log.error("issue1583调账：更新delegation表和address表失败,数据为:[{}]", JSONUtil.toJsonStr(list));
            throw e;
        }
    }

}
