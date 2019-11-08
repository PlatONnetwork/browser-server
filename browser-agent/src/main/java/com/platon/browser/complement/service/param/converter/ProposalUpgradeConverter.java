package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.proposal.ProposalUpgrade;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.ProposalBusinessMapper;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.param.ProposalUpgradeParam;
import com.platon.browser.util.RoundCalculation;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalUpgradeConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
	
    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
    	ProposalUpgradeParam txParam = tx.getTxParam(ProposalUpgradeParam.class);

    	ProposalUpgrade businessParam= ProposalUpgrade.builder()
    			.nodeId(txParam.getVerifier())
    			.pIDID(txParam.getPIDID())
    			.url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
    			.pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
    			.endVotingBlock(RoundCalculation.endBlockNumCal(tx.getNum().toString(),chainConfig.getProposalTextConsensusRounds(),chainConfig).toBigInteger())
    			.activeBlock(RoundCalculation.activeBlockNumCal(tx.getNum().toString(), txParam.getEndVotingRound(), chainConfig).toBigInteger())
    			.topic(CustomProposal.QUERY_FLAG)
    			.description(CustomProposal.QUERY_FLAG)
    			.txHash(tx.getHash())
    			.blockNumber(BigInteger.valueOf(tx.getNum()))
    			.timestamp(tx.getTime())
    			.stakingName(txParam.getNodeName())
    			.newVersion(String.valueOf(txParam.getNewVersion()))
                .build();
    	



    	proposalBusinessMapper.upgrade(businessParam);


		String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
				.replace("ID",txParam.getPIDID())
				.replace("TITLE",businessParam.getTopic())
				.replace("TYPE",CustomProposal.TypeEnum.UPGRADE.getCode())
				.replace("VERSION",businessParam.getNewVersion());


		ComplementNodeOpt c = ComplementNodeOpt.newInstance();
		c.setNodeId(txParam.getVerifier());
		c.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.PROPOSALS.getCode()));
		c.setDesc(desc);
		c.setTxHash(tx.getHash());
		c.setBNum(event.getBlock().getNum());
		c.setTime(event.getBlock().getTime());
        return Optional.ofNullable(c);
    }
}
