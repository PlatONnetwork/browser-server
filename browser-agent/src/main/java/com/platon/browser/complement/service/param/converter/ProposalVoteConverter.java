package com.platon.browser.complement.service.param.converter;

import java.math.BigInteger;
import java.util.Optional;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.complement.param.proposal.ProposalVote;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.mapper.ProposalBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.param.ProposalVoteParam;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Service
public class ProposalVoteConverter extends BusinessParamConverter<Optional<ComplementNodeOpt>> {
	
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
	
    @Override
    public Optional<ComplementNodeOpt> convert(CollectionEvent event, CollectionTransaction tx) {
    	ProposalVoteParam txParam = tx.getTxParam(ProposalVoteParam.class);

    	ProposalVote businessParam= ProposalVote.builder()
    			.nodeId(txParam.getVerifier())
    			.txHash(tx.getHash())
    			.bNum(BigInteger.valueOf(tx.getNum()))
    			.timestamp(tx.getTime())
    			.stakingName(txParam.getNodeName())
    			.proposalHash(txParam.getProposalId())
    			.voteOption(Integer.valueOf(txParam.getOption()))
                .build();
    	


    	proposalBusinessMapper.vote(businessParam);

		String desc = CustomNodeOpt.TypeEnum.VOTE.getTpl()
				.replace("ID",txParam.getProposalId())
				.replace("TITLE","") //TODO
				.replace("OPTION",txParam.getOption())
				.replace("TYPE", "")//TODO
				.replace("VERSION","");//TODO

		ComplementNodeOpt c = ComplementNodeOpt.newInstance();
		c.setNodeId(txParam.getVerifier());
		c.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.VOTE.getCode()));
		c.setDesc(desc);
		c.setTxHash(tx.getHash());
		c.setBNum(event.getBlock().getNum());
		c.setTime(event.getBlock().getTime());
        return Optional.ofNullable(c);
    }
}
