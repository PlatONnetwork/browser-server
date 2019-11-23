package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.NewBlockMapper;
import com.platon.browser.complement.dao.param.epoch.NewBlock;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class OnNewBlockConverter {
	
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NewBlockMapper newBlockMapper;

    @Autowired
    private NetworkStatCache networkStatCache;

    private long blockCount=0;
	public void convert(CollectionEvent event, Block block) throws NoSuchBeanException {

        long startTime = System.currentTimeMillis();

	    networkStatCache.getNetworkStat().setCurNumber(event.getBlock().getNum());
		NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(event.getEpochMessage().getPreBlockReward())
                .feeRewardValue(new BigDecimal(block.getTxFee()))
                .predictStakingReward(event.getEpochMessage().getStakeReward())
                .build();
        
		newBlockMapper.newBlock(newBlock);

        if(block.getNodeId().equals("0xff40ac420279ddbe58e1bf1cfe19f4b5978f86e7c483223be26e80ac9790e855cb5d7bd743d94b9bd72be79f01ee068bc1fefe79c06ba9cd49fa96f52c7bdce0")) {
            ++blockCount;
            log.error("block count:{}",blockCount);
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
	}

}
