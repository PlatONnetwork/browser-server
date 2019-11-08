package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.complement.dao.param.epoch.NewBlock;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.NewBlockMapper;
import com.platon.browser.elasticsearch.dto.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OnNewBlockConverter {
	
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NewBlockMapper newBlockMapper;

	public void convert(CollectionEvent event, Block block) throws Exception {
      
		NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(new BigDecimal(event.getEpochMessage().getBlockReward()))
                .feeRewardValue(block.getTxFee())
                .build();
        
		newBlockMapper.newBlock(newBlock); 
	}

}
