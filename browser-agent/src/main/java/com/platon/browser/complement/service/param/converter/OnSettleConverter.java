package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.param.epoch.Settle;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.persistence.dao.mapper.EpochBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OnSettleConverter {
	
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
	
	public Settle convert(CollectionEvent event,CollectionBlock block) throws Exception {
        List<String> curVerifierList = new ArrayList<>();
        event.getEpochMessage().getCurVerifierList().forEach(v->curVerifierList.add(v.getNodeId()));
        List<String> preVerifierList = new ArrayList<>();
        event.getEpochMessage().getPreVerifierList().forEach(v->preVerifierList.add(v.getNodeId()));
        
        Settle settle = Settle.builder()
                .preVerifierList(preVerifierList)
                .curVerifierList(curVerifierList)
                .stakingReward(new BigDecimal(event.getEpochMessage().getStakeReward()))
                .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
                .stakingLockEpoch(chainConfig.getUnStakeRefundSettlePeriodCount().intValue())
                .build();
        
        epochBusinessMapper.settle(settle);
       
		return settle;
	}

}
