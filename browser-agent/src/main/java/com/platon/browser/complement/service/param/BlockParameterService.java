package com.platon.browser.complement.service.param;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.dto.BusinessParam;
import com.platon.browser.common.complement.dto.epoch.Consensus;
import com.platon.browser.common.complement.dto.epoch.Election;
import com.platon.browser.common.complement.dto.epoch.Settle;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.queue.collection.event.CollectionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event){
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();

        List<String> preVerifierList = event.getEpochMessage().getPreVerifiers();

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
            List<String> validatorList = event.getEpochMessage().getCurValidators();
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
            Settle settle = Settle.builder()
                    .preVerifierList(preVerifierList)
                    .curVerifierList(event.getEpochMessage().getCurVerifiers())
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
