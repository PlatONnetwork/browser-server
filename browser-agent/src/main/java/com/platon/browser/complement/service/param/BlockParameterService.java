package com.platon.browser.complement.service.param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.epoch.Consensus;
import com.platon.browser.common.complement.dto.epoch.Election;
import com.platon.browser.common.complement.dto.epoch.NewBlock;
import com.platon.browser.common.complement.dto.epoch.Settle;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;

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

    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event) throws Exception{
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();

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
            event.getEpochMessage().getPreVerifiers().forEach(v->preVerifierList.add(v.getNodeId()));
            
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
            event.getEpochMessage().getCurValidators().forEach(v->validatorList.add(v.getNodeId()));

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
            event.getEpochMessage().getCurVerifiers().forEach(v->curVerifierList.add(v.getNodeId()));
            List<String> preVerifierList = new ArrayList<>();
            event.getEpochMessage().getPreVerifiers().forEach(v->preVerifierList.add(v.getNodeId()));
            
            Settle settle = Settle.builder()
                    .preVerifierList(preVerifierList)
                    .curVerifierList(curVerifierList)
                    .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                    // TODO: 年化率计算
                    //.annualizedRate() // 年化率
                    //.annualizedRateInfo() // 年化率信息
                    //.feeRewardValue() // 交易手续费
                    //.stakingLockEpoch() // 质押锁定的结算周期数
                    .build();
            businessParams.add(settle);
        }

        if (block.getNum() % chainConfig.getAddIssuePeriodBlockCount().longValue() == 0) {
            log.debug("增发周期切换：Block Number({})", block.getNum());
            // TODO: 增发周期切换
        }
        return businessParams;
    }
}
