package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.param.epoch.NewBlock;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.NewBlockMapper;

@Service
public class OnNewBlockConverter {
	
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NewBlockMapper newBlockMapper;

	public void convert(CollectionEvent event,CollectionBlock block) throws Exception {
      
		NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(new BigDecimal(event.getEpochMessage().getBlockReward()))
                .feeRewardValue(block.getTxFee())
                .build();
        
		newBlockMapper.newBlock(newBlock); 
	}

}
