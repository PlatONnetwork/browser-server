package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.dto.epoch.Election;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.persistence.dao.mapper.EpochBusinessMapper;

public class OnElectionConverter {
	
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
	
	public Election convert(CollectionEvent event,CollectionBlock block) throws Exception {
	   List<String> preValidatorList = new ArrayList<>();
       event.getEpochMessage().getPreValidatorList().forEach(v->preValidatorList.add(v.getNodeId()));
       
       Election election = Election.builder()
               .bNum(BigInteger.valueOf(block.getNum()))
               .time(block.getTime())
               .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
               .preValidatorList(preValidatorList)
               .build();
       
       epochBusinessMapper.election(election);
       
       return election;
	}

}
