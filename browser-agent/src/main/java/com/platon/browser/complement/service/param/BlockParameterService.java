package com.platon.browser.complement.service.param;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.epoch.Consensus;
import com.platon.browser.common.complement.dto.epoch.Election;
import com.platon.browser.common.complement.dto.epoch.NewBlock;
import com.platon.browser.common.complement.dto.epoch.Settle;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    public List<BusinessParam> getParameters(CollectionEvent event){
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();

        // 新区块事件
        try {
            NodeItem nodeItem = nodeCache.getNode(block.getNodeId());
            NewBlock newBlock = NewBlock.builder()
                    .nodeId(block.getNodeId())
                    .blockRewardValue(new BigDecimal(event.getEpochMessage().getBlockReward()))
                    .feeRewardValue(block.getTxFee())
                    .stakingBlockNum(nodeItem.getStakingBlockNum())
                    .build();
            businessParams.add(newBlock);
        } catch (NoSuchBeanException e) {
            throw new BusinessException("致命错误:每个区块必定由一个被质押的节点产生,因此必定有质押区块!");
        }


        List<String> preVerifierList = new ArrayList<>();
        event.getEpochMessage().getPreVerifiers().forEach(v->preVerifierList.add(v.getNodeId()));
        if ((block.getNum()+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.debug("选举验证人：Block Number({})", block.getNum());
            Election election = Election.builder()
                    .bNum(BigInteger.valueOf(block.getNum()))
                    .time(block.getTime())
                    .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                    .preVerifierList(preVerifierList)
                    .build();
            businessParams.add(election);
        }

        if (block.getNum() % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.debug("共识周期切换：Block Number({})", block.getNum());
            List<String> validatorList = new ArrayList<>();
            event.getEpochMessage().getCurValidators().forEach(v->validatorList.add(v.getNodeId()));

            BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(validatorList.size()));
            Consensus consensus = Consensus.builder()
                    .nodeId(block.getNodeId())
                    .expectBlockNum(expectBlockNum)
                    .validatorList(validatorList)
                    .build();
            businessParams.add(consensus);
        }

        if (block.getNum() % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            log.debug("结算周期切换：Block Number({})", block.getNum());
            List<String> curVerifierList = new ArrayList<>();
            event.getEpochMessage().getCurVerifiers().forEach(v->curVerifierList.add(v.getNodeId()));
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
