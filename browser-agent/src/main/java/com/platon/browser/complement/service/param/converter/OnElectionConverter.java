package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionBlock;
import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.epoch.Election;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.EpochBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;

@Service
public class OnElectionConverter {
	
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
	
	public Optional<List<ComplementNodeOpt>> convert(CollectionEvent event,CollectionBlock block) throws Exception {
		//前一周期共识验证人
		List<String> preValidatorList = event.getEpochMessage().getPreValidatorList().stream()
				.map(node -> node.getNodeId()).collect(Collectors.toList());
		if(preValidatorList.size() == 0) {
			return Optional.ofNullable(null);
		}
	
		//查询需要惩罚的节点
		List<String> slashNodeList = epochBusinessMapper.querySlashNode(preValidatorList);
		if(slashNodeList.size() == 0) {
			return Optional.ofNullable(null);
		}
		
		//惩罚节点
		Election election = Election.builder()
				.time(block.getTime())
				.settingEpoch(event.getEpochMessage().getSettleEpochRound().intValue())
				.slashNodeList(slashNodeList)
				.build();
		epochBusinessMapper.slashNode(election);
		
		//节点操作日志
		BigInteger bNum = BigInteger.valueOf(block.getNum());
		List<ComplementNodeOpt> complementNodeOpts = slashNodeList.stream()
				.map(node -> {
					ComplementNodeOpt complementNodeOpt = ComplementNodeOpt.newInstance();
					complementNodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
					complementNodeOpt.setNodeId(node);
					complementNodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
					complementNodeOpt.setBNum(bNum.longValue());
					complementNodeOpt.setTime(block.getTime());
					complementNodeOpt.setDesc("0|0|0|1");
					return complementNodeOpt;
				}).collect(Collectors.toList());
		
       return Optional.ofNullable(complementNodeOpts);
	}

}
