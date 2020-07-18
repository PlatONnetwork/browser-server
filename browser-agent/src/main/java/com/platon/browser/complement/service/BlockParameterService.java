package com.platon.browser.complement.service;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.epoch.OnConsensusConverter;
import com.platon.browser.complement.converter.epoch.OnElectionConverter;
import com.platon.browser.complement.converter.epoch.OnNewBlockConverter;
import com.platon.browser.complement.converter.epoch.OnSettleConverter;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<NodeOpt> getParameters(CollectionEvent event) throws NoSuchBeanException, IOException, BlockNumberException {
        long startTime = System.currentTimeMillis();

        List<NodeOpt> nodeOptList = new ArrayList<>();
        Block block = event.getBlock();

        if(block.getNum()==0) return nodeOptList;

        // 新选举周期事件
        if ((block.getNum()+chainConfig.getElectionBackwardBlockCount().longValue()) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0
                &&event.getEpochMessage().getConsensusEpochRound().longValue()>1) {
            // 共识轮数等于大于1的时候才进来
            log.debug("选举验证人：Block Number({})", block.getNum());
            List<NodeOpt> nodeOpt = onElectionConverter.convert(event, block);
            nodeOptList.addAll(nodeOpt);
        }

        // 新共识周期事件
        if ((block.getNum()-1) % chainConfig.getConsensusPeriodBlockCount().longValue() == 0) {
            log.debug("共识周期切换：Block Number({})", block.getNum());
            Optional<List<NodeOpt>> nodeOpt = onConsensusConverter.convert(event, block);
            nodeOpt.ifPresent(nodeOptList::addAll);
        }

        // 新结算周期事件
        if ((block.getNum()-1) % chainConfig.getSettlePeriodBlockCount().longValue() == 0) {
            log.debug("结算周期切换：Block Number({})", block.getNum());
            List<NodeOpt> nodeOpt = onSettleConverter.convert(event, block);
            nodeOptList.addAll(nodeOpt);
        }

        // 新区块事件
        onNewBlockConverter.convert(event,block);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return nodeOptList;
    }
}
