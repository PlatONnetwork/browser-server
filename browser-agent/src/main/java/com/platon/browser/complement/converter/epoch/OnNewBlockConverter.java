package com.platon.browser.complement.converter.epoch;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.ParamProposalCache;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.common.service.proposal.ProposalService;
import com.platon.browser.complement.dao.mapper.NewBlockMapper;
import com.platon.browser.complement.dao.param.epoch.NewBlock;
import com.platon.browser.dao.entity.Config;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.service.govern.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.platon.bean.TallyResult;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class OnNewBlockConverter {
	
    @Autowired
    private NodeCache nodeCache;
    
    @Autowired
    private NewBlockMapper newBlockMapper;

    @Autowired
    private NetworkStatCache networkStatCache;
    @Autowired
    private ParamProposalCache paramProposalCache;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private ProposalMapper proposalMapper;
    @Autowired
    private ParameterService parameterService;

	public void convert(CollectionEvent event, Block block) throws NoSuchBeanException {

        long startTime = System.currentTimeMillis();

	    networkStatCache.getNetworkStat().setCurNumber(event.getBlock().getNum());
		NewBlock newBlock = NewBlock.builder()
                .nodeId(block.getNodeId())
                .stakingBlockNum(nodeCache.getNode(block.getNodeId()).getStakingBlockNum())
                .blockRewardValue(event.getEpochMessage().getBlockReward())
                .feeRewardValue(new BigDecimal(block.getTxFee()))
                .predictStakingReward(event.getEpochMessage().getStakeReward())
                .build();
        
		newBlockMapper.newBlock(newBlock);

		// 检查当前区块是否有参数提案生效
        Set<String> proposalTxHashSet = paramProposalCache.get(block.getNum());
        if(proposalTxHashSet!=null){
            ProposalExample proposalExample = new ProposalExample();
            proposalExample.createCriteria().andHashIn(new ArrayList<>(proposalTxHashSet));
            List<Proposal> proposalList = proposalMapper.selectByExample(proposalExample);
            Map<String,Proposal> proposalMap = new HashMap<>();
            proposalList.forEach(p->proposalMap.put(p.getHash(),p));
            List<Config> configList = new ArrayList<>();
            for (String hash : proposalTxHashSet) {
                try {
                    TallyResult tr = proposalService.getTallyResult(hash);
                    if(tr.getStatus()== CustomProposal.StatusEnum.FINISH.getCode()){
                        // 提案生效：
                        // 把提案表中的参数覆盖到Config表中对应的参数
                        Proposal proposal = proposalMap.get(hash);
                        Config config = new Config();
                        config.setModule(proposal.getModule());
                        config.setName(proposal.getName());
                        config.setValue(proposal.getNewValue());
                        configList.add(config);
                    }
                } catch (Exception e) {
                    throw new BusinessException(e.getMessage());
                }
            }
            if(!configList.isEmpty()) {
                // 更新配置表config及内存中的blockChainConfig
                parameterService.rotateConfig(configList);
            }
        }

        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
	}

}
