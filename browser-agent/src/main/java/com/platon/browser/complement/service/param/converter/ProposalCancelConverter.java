package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.param.proposal.ProposalCancel;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.param.ProposalCancelParam;
import com.platon.browser.persistence.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.util.RoundCalculation;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalCancelConverter extends BusinessParamConverter<ProposalCancel> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
	
    @Override
    public ProposalCancel convert(CollectionEvent event, CollectionTransaction tx) {
    	ProposalCancelParam txParam = tx.getTxParam(ProposalCancelParam.class);

    	ProposalCancel businessParam= ProposalCancel.builder()
    			.nodeId(txParam.getVerifier())
    			.pIDID(txParam.getPIDID())
    			.url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
    			.pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
    			.endVotingBlock(RoundCalculation.endBlockNumCal(tx.getNum().toString(),chainConfig.getProposalTextConsensusRounds(),chainConfig).toBigInteger())
    			.topic(CustomProposal.QUERY_FLAG)
    			.description(CustomProposal.QUERY_FLAG)
    			.txHash(tx.getHash())
    			.blockNumber(BigInteger.valueOf(tx.getNum()))
    			.timestamp(tx.getTime())
    			.stakingName(txParam.getNodeName())
    			.canceledId(txParam.getCanceledProposalID())
                .build();
    	
    	String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
				.replace("ID",txParam.getPIDID())
				.replace("TITLE",businessParam.getTopic())
				.replace("TYPE",CustomProposal.TypeEnum.CANCEL.getCode())
				.replace("VERSION","");
 
    	businessParam.setOptDesc(desc);
    	
    	proposalBusinessMapper.cancel(businessParam);
    	
        return businessParam;
    }
}
