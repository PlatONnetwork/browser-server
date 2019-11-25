package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.complement.dao.param.proposal.ProposalParameter;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalParameterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

/**
 * @description: 参数提案业务参数转换器
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalParameterConverter extends BusinessParamConverter<Optional<NodeOpt>> {

    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private ProposalBusinessMapper proposalBusinessMapper;
    @Autowired
    private NetworkStatCache networkStatCache;
	
    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) {
		ProposalParameterParam txParam = tx.getTxParam(ProposalParameterParam.class);
		// 补充节点名称
		updateTxInfo(txParam,tx);
		// 失败的交易不分析业务数据
		if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

		long startTime = System.currentTimeMillis();

		ProposalParameter businessParam= ProposalParameter.builder()
    			.nodeId(txParam.getVerifier())
    			.pIDID(txParam.getPIDID())
    			.url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
    			.pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
//    			.endVotingBlock(RoundCalculation.endBlockNumCal(tx.getNum().toString(),txParam.getEndVotingRound(),chainConfig).toBigInteger())
//    			.activeBlock(RoundCalculation.activeBlockNumCal(tx.getNum().toString(), txParam.getEndVotingRound(), chainConfig).toBigInteger())
    			.topic(CustomProposal.QUERY_FLAG)
    			.description(CustomProposal.QUERY_FLAG)
    			.txHash(tx.getHash())
    			.blockNumber(BigInteger.valueOf(tx.getNum()))
    			.timestamp(tx.getTime())
    			.stakingName(txParam.getNodeName())
				.module(txParam.getModule())
				.name(txParam.getName())
				.newValue(txParam.getNewValue())
                .build();
    	

    	proposalBusinessMapper.parameter(businessParam);


		String desc = NodeOpt.TypeEnum.PROPOSALS.getTpl()
				.replace("ID",txParam.getPIDID())
				.replace("TITLE",businessParam.getTopic())
				.replace("TYPE",String.valueOf(CustomProposal.TypeEnum.PARAMETER.getCode()))
				.replace("MODULE",businessParam.getModule())
				.replace("PARAMETER",businessParam.getName());


		NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
		nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(txParam.getVerifier());
		nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.PROPOSALS.getCode()));
		nodeOpt.setDesc(desc);
		nodeOpt.setTxHash(tx.getHash());
		nodeOpt.setBNum(event.getBlock().getNum());
		nodeOpt.setTime(event.getBlock().getTime());

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}
