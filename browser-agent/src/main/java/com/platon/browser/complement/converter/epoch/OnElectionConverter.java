package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.dao.param.epoch.Election;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.EpochBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.bean.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OnElectionConverter {
	
    @Autowired
    private EpochBusinessMapper epochBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
	
	public Optional<List<NodeOpt>> convert(CollectionEvent event, Block block) throws Exception {

		long startTime = System.currentTimeMillis();

		//前一周期共识验证人
		List<String> preValidatorList = event.getEpochMessage().getPreValidatorList().stream()
				.map(Node::getNodeId).collect(Collectors.toList());
		if(preValidatorList.isEmpty()) {
			return Optional.ofNullable(null);
		}
	
		//查询需要惩罚的节点
		List<String> slashNodeList = epochBusinessMapper.querySlashNode(preValidatorList);
		if(slashNodeList.isEmpty()) {
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
		List<NodeOpt> nodeOpts = slashNodeList.stream()
				.map(node -> {
					NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
					nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
					nodeOpt.setNodeId(node);
					nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.LOW_BLOCK_RATE.getCode()));
					nodeOpt.setBNum(bNum.longValue());
					nodeOpt.setTime(block.getTime());
					nodeOpt.setDesc("0|0|0|1");
					return nodeOpt;
				}).collect(Collectors.toList());

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

       return Optional.ofNullable(nodeOpts);
	}

}
