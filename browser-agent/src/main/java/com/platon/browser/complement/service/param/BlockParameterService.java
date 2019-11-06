package com.platon.browser.complement.service.param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.collection.dto.EpochMessage;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.epoch.Consensus;
import com.platon.browser.common.complement.dto.epoch.Election;
import com.platon.browser.common.complement.dto.epoch.NewBlock;
import com.platon.browser.common.complement.dto.epoch.Settle;
import com.platon.browser.common.complement.dto.statistic.AddressStatChange;
import com.platon.browser.common.complement.dto.statistic.NetworkStatChange;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.utils.CalculateUtils;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.NetworkStat;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 业务入库参数服务
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class BlockParameterService {

    @Autowired
    private BlockChainConfig chainConfig;
    
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NetworkStatCache networkStatCache;

    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event) throws Exception{
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();
        EpochMessage epochMessage = event.getEpochMessage();

        // 新区块事件
        NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(new BigDecimal(event.getEpochMessage().getBlockReward()))
                .feeRewardValue(block.getTxFee())
                .build();
        businessParams.add(newBlock);

        // 新选举周期事件
        if ((block.getNum()+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0
                &&event.getEpochMessage().getConsensusEpochRound().longValue()>1) {
            // 共识轮数等于大于1的时候才进来
            log.debug("选举验证人：Block Number({})", block.getNum());
            List<String> preVerifierList = new ArrayList<>();
            event.getEpochMessage().getPreVerifierList().forEach(v->preVerifierList.add(v.getNodeId()));
            
            Election election = Election.builder()
                    .bNum(BigInteger.valueOf(block.getNum()))
                    .time(block.getTime())
                    .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                    .preVerifierList(preVerifierList)
                    .build();
            businessParams.add(election);
        }

        // 新共识周期事件
        if (block.getNum() % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.debug("共识周期切换：Block Number({})", block.getNum());
            List<String> validatorList = new ArrayList<>();
            event.getEpochMessage().getCurValidatorList().forEach(v->validatorList.add(v.getNodeId()));

            BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(validatorList.size()));
            Consensus consensus = Consensus.builder()
                    .expectBlockNum(expectBlockNum)
                    .validatorList(validatorList)
                    .build();
            businessParams.add(consensus);
        }

        // 新结算周期事件
        if (block.getNum() % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            log.debug("结算周期切换：Block Number({})", block.getNum());
            List<String> curVerifierList = new ArrayList<>();
            event.getEpochMessage().getCurVerifierList().forEach(v->curVerifierList.add(v.getNodeId()));
            List<String> preVerifierList = new ArrayList<>();
            event.getEpochMessage().getPreVerifierList().forEach(v->preVerifierList.add(v.getNodeId()));
            
            Settle settle = Settle.builder()
                    .preVerifierList(preVerifierList)
                    .curVerifierList(curVerifierList)
                    .stakingReward(new BigDecimal(event.getEpochMessage().getStakeReward()))
                    .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                    .stakingLockEpoch(chainConfig.getUnStakeRefundSettlePeriodCount().intValue())
                    .build();
            businessParams.add(settle);
        }

        // 网络统计
        NetworkStat networkStat = networkStatCache.getNetworkStat();
        NetworkStatChange networkStatChange = NetworkStatChange.builder()
        	.id(1)
        	.curNumber(block.getNum())
        	.nodeId(block.getNodeId())
        	.nodeName(nodeCache.getNode(block.getNodeId()).getNodeName())
        	.txQty(networkStat.getTxQty())
        	.curTps(networkStat.getCurTps())
        	.maxTps(networkStat.getMaxTps())
        	.proposalQty(networkStat.getProposalQty())
        	.blockReward(new BigDecimal(epochMessage.getBlockReward()))
        	.stakingReward(new BigDecimal(epochMessage.getStakeReward()))
        	.addIssueBegin(CalculateUtils.calculateAddIssueBegin(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.addIssueEnd(CalculateUtils.calculateAddIssueEnd(chainConfig.getAddIssuePeriodBlockCount(), epochMessage.getIssueEpochRound()))
        	.nextSettle(CalculateUtils.calculateNextSetting(chainConfig.getSettlePeriodBlockCount(), epochMessage.getSettleEpochRound(), epochMessage.getCurrentBlockNumber()))
        	.build();
        businessParams.add(networkStatChange);
        
        // 地址统计
        AddressStatChange addressStatChange = AddressStatChange.builder()
        		
        		.build();
        businessParams.add(addressStatChange);
        return businessParams;
    }
}
