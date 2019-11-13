package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.complement.dao.param.epoch.Consensus;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OnConsensusConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
	
	public void convert(CollectionEvent event, Block block) {
        long startTime = System.currentTimeMillis();

        log.debug("Block Number:{}",block.getNum());

        List<String> validatorList = new ArrayList<>();
        event.getEpochMessage().getCurValidatorList().forEach(v->validatorList.add(v.getNodeId()));

        BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(validatorList.size()));
        Consensus consensus = Consensus.builder()
                .expectBlockNum(expectBlockNum)
                .validatorList(validatorList)
                .build();
       
        epochBusinessMapper.consensus(consensus);

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
	}

}
