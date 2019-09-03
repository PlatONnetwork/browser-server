package com.platon.browser.engine.handler.statistic;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomBlock;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

import static com.platon.browser.dto.CustomProposal.StatusEnum;
import static com.platon.browser.dto.CustomProposal.TypeEnum;
import static com.platon.browser.engine.BlockChain.*;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:09
 * @Description: 统计信息处理类
 */
@Component
public class NetworkStatStatisticHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(NetworkStatStatisticHandler.class);

    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;

    @Override
    public void handle ( EventContext context ) {
        CustomBlock curBlock = bc.getCurBlock();

        try {
            CustomNode curNode = NODE_CACHE.getNode(curBlock.getNodeId());
            CustomStaking curStaking = curNode.getLatestStaking();
            //TODO:地址数需要地址统计
            //当前区块高度
            NETWORK_STAT_CACHE.setCurrentNumber(curBlock.getNumber());
            //当前区块所属节点id
            NETWORK_STAT_CACHE.setNodeId(curBlock.getNodeId());
            //当前区块所属节点name
            NETWORK_STAT_CACHE.setNodeName(curStaking.getStakingName()==null ? "Unknown" : curStaking.getStakingName());
            //TODO:可优化
            //当前增发周期结束块高 =  每个增发周期块数 *  当前增发周期轮数
            NETWORK_STAT_CACHE.setAddIssueEnd(chainConfig.getAddIssuePeriodBlockCount().multiply(bc.getAddIssueEpoch()).longValue());
            //TODO:可优化
            //当前增发周期开始块高 = (每个增发周期块数 * 当前增发周期轮数) - 每个增发周期块数
            NETWORK_STAT_CACHE.setAddIssueBegin(chainConfig.getAddIssuePeriodBlockCount().multiply(bc.getAddIssueEpoch()).subtract(chainConfig.getAddIssuePeriodBlockCount()).longValue());
            //离下个结算周期剩余块高 = (每个结算周期块数 * 当前结算周期轮数) - 当前块高
            NETWORK_STAT_CACHE.setNextSetting(chainConfig.getSettlePeriodBlockCount().multiply(bc.getCurSettingEpoch()).subtract(curBlock.getBlockNumber()).longValue());
            //质押奖励
            NETWORK_STAT_CACHE.setStakingReward(bc.getSettleReward().divide(new BigDecimal(bc.getPreVerifier().size()),0,BigDecimal.ROUND_DOWN).toString());

            //更新时间
            NETWORK_STAT_CACHE.setUpdateTime(new Date());
            //累计交易总数
            NETWORK_STAT_CACHE.setTxQty(NETWORK_STAT_CACHE.getTxQty() + curBlock.getStatTxQty());
            //当前区块交易总数
            NETWORK_STAT_CACHE.setCurrentTps(curBlock.getStatTxQty());
            //已统计区块中最高交易个数
            NETWORK_STAT_CACHE.setMaxTps(NETWORK_STAT_CACHE.getMaxTps() > curBlock.getStatTxQty() ? NETWORK_STAT_CACHE.getMaxTps() : curBlock.getStatTxQty());
            //出块奖励
            NETWORK_STAT_CACHE.setBlockReward(bc.getBlockReward().toString());
            if (curBlock.getStatProposalQty() > 0) {
                //累计提案总数
                NETWORK_STAT_CACHE.setProposalQty(NETWORK_STAT_CACHE.getProposalQty() + curBlock.getStatProposalQty());
            }
            if (curBlock.getStatStakingQty() > 0) {
                //统计质押金额
                Set <CustomStaking> newStaking = STAGE_DATA.getStakingStage().getStakingInsertStage();
                newStaking.forEach(staking -> {
                    BigInteger stakingValue = NETWORK_STAT_CACHE.integerStakingValue().add(staking.integerStakingHas()).add(staking.integerStakingLocked());
                    NETWORK_STAT_CACHE.setStakingValue(stakingValue.toString());
                });
            }
            if (curBlock.getStatDelegateQty() > 0) {
                //质押已统计，本次累加上委托
                Set <CustomDelegation> newDelegation = STAGE_DATA.getStakingStage().getDelegationInsertStage();
                newDelegation.forEach(delegation -> {
                    //先做委托累加
                    BigInteger delegationValue = delegation.integerDelegateHas().add(delegation.integerDelegateLocked()).add(NETWORK_STAT_CACHE.integerStakingDelegationValue());
                    NETWORK_STAT_CACHE.setStakingDelegationValue(delegationValue.toString());
                });
                //在累加计算好的质押金
                NETWORK_STAT_CACHE.setStakingDelegationValue(NETWORK_STAT_CACHE.integerStakingDelegationValue().add(NETWORK_STAT_CACHE.integerStakingValue()).toString());
            }
            /**
             * 进行中提案统计，根据不同类型区分：
             *  1.文本提案：状态为投票中的为进行中的提案
             *  2.升级提案：状态为投票中、预升级、为进行中提案
             *  3.取消提案：状态为投票中的为进行中的提案
             */
            NETWORK_STAT_CACHE.setDoingProposalQty(0);
            PROPOSALS_CACHE.getAllProposal().forEach(proposal -> {
                if (proposal.getStatus().equals(StatusEnum.VOTING.code)) {
                    NETWORK_STAT_CACHE.setDoingProposalQty(NETWORK_STAT_CACHE.getDoingProposalQty() + 1);
                }
                if (proposal.getType().equals(TypeEnum.UPGRADE.code)) {
                    if (proposal.getStatus().equals(StatusEnum.PASS.code) || proposal.getStatus().equals(StatusEnum.PRE_UPGRADE.code)) {
                        NETWORK_STAT_CACHE.setDoingProposalQty(NETWORK_STAT_CACHE.getDoingProposalQty() + 1);
                    }
                }
            });

            //更新暂存变量
            STAGE_DATA.getNetworkStatStage().updateNetworkStat(NETWORK_STAT_CACHE);
        } catch (NoSuchBeanException e) {
            logger.error("-------------------------[NETWORK_STAT_CACHE]-------------------------- {}", curBlock.getBlockNumber());
            logger.error("{}", e.getMessage());
        }
    }
}
