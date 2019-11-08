package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.param.epoch.Election;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.persistence.dao.mapper.EpochBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
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
