package com.platon.browser.complement.service.param;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.param.BusinessParam;
import com.platon.browser.common.complement.param.epoch.Consensus;
import com.platon.browser.common.complement.param.epoch.Election;
import com.platon.browser.common.complement.param.epoch.NewBlock;
import com.platon.browser.common.complement.param.epoch.Settle;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.service.param.converter.OnConsensusConverter;
import com.platon.browser.complement.service.param.converter.OnElectionConverter;
import com.platon.browser.complement.service.param.converter.OnNewBlockConverter;
import com.platon.browser.complement.service.param.converter.OnSettleConverter;
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
    private OnNewBlockConverter onNewBlockConverter;
    @Autowired
    private OnElectionConverter onElectionConverter;
    @Autowired
    private OnConsensusConverter onConsensusConverter;
    @Autowired
    private OnSettleConverter onSettleConverter;
    
    /**
     * 解析区块, 构造业务入库参数信息
     * @return
     */
    public List<BusinessParam> getParameters(CollectionEvent event) throws Exception{
        List<BusinessParam> businessParams = new ArrayList<>();
        CollectionBlock block = event.getBlock();

        // 新区块事件
        NewBlock newBlock = onNewBlockConverter.convert(event,block);
        businessParams.add(newBlock);

        // 新选举周期事件
        if ((block.getNum()+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0
                &&event.getEpochMessage().getConsensusEpochRound().longValue()>1) {
            // 共识轮数等于大于1的时候才进来
            log.debug("选举验证人：Block Number({})", block.getNum());
            Election election = onElectionConverter.convert(event, block);
            businessParams.add(election);
        }

        // 新共识周期事件
        if (block.getNum() % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.debug("共识周期切换：Block Number({})", block.getNum());
            Consensus consensus = onConsensusConverter.convert(event, block);
            businessParams.add(consensus);
        }

        // 新结算周期事件
        if (block.getNum() % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            log.debug("结算周期切换：Block Number({})", block.getNum());
            Settle settle = onSettleConverter.convert(event, block);
            businessParams.add(settle);
        }
        return businessParams;
    }
}
