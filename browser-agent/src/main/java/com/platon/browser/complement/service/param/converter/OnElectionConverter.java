package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.epoch.Election;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.EpochBusinessMapper;

@Service
public class OnElectionConverter {
	
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
	
	public Optional<ComplementNodeOpt> convert(CollectionEvent event,CollectionBlock block) throws Exception {
	   List<String> preValidatorList = new ArrayList<>();
       event.getEpochMessage().getPreValidatorList().forEach(v->preValidatorList.add(v.getNodeId()));
       
       Election election = Election.builder()
               .bNum(BigInteger.valueOf(block.getNum()))
               .time(block.getTime())
               .settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
               .preValidatorList(preValidatorList)
               .build();
       
       //TODO 添加计算逻辑
       
       epochBusinessMapper.election(election);
       
       return Optional.ofNullable(null);
	}

}
