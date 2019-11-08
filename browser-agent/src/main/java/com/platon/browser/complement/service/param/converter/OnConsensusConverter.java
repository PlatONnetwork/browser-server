package com.platon.browser.complement.service.param.converter;

import com.platon.browser.complement.dao.param.epoch.Consensus;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.elasticsearch.dto.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class OnConsensusConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
	
	public void convert(CollectionEvent event, Block block) throws Exception {
        List<String> validatorList = new ArrayList<>();
        event.getEpochMessage().getCurValidatorList().forEach(v->validatorList.add(v.getNodeId()));

        BigInteger expectBlockNum = chainConfig.getConsensusPeriodBlockCount().divide(BigInteger.valueOf(validatorList.size()));
        Consensus consensus = Consensus.builder()
                .expectBlockNum(expectBlockNum)
                .validatorList(validatorList)
                .build();
       
        epochBusinessMapper.consensus(consensus);
	}

}
