package com.platon.browser.complement.service.param.converter;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.dto.epoch.NewBlock;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.persistence.dao.mapper.NewBlockMapper;

public class OnNewBlockConverter {
	
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NewBlockMapper newBlockMapper;

	public NewBlock convert(CollectionEvent event,CollectionBlock block) throws Exception {
      
		NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(new BigDecimal(event.getEpochMessage().getBlockReward()))
                .feeRewardValue(block.getTxFee())
                .build();
        
		newBlockMapper.newBlock(newBlock);  
        
		return newBlock;
	}

}
