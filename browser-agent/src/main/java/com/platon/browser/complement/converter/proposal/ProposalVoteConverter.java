package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.param.proposal.ProposalVote;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalVoteParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalVoteConverter extends BusinessParamConverter<Optional<NodeOpt>> {
	
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
		long startTime = System.currentTimeMillis();

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

		NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
		nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getVerifier());
		nodeOpt.setType(Integer.valueOf(CustomNodeOpt.TypeEnum.VOTE.getCode()));
		nodeOpt.setDesc(desc);
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(event.getBlock().getNum());
		nodeOpt.setTime(event.getBlock().getTime());

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}
