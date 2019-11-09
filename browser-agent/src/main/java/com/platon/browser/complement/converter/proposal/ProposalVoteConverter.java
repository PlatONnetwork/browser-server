package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.param.proposal.ProposalVote;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.ProposalVoteParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) throws NoSuchBeanException {
		ProposalVoteParam txParam = tx.getTxParam(ProposalVoteParam.class);
		String nodeId=txParam.getVerifier();
		NodeItem nodeItem = nodeCache.getNode(nodeId);
		txParam.setNodeName(nodeItem.getNodeName());
		tx.setInfo(txParam.toJSONString());
		// 失败的交易不分析业务数据
		if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

		long startTime = System.currentTimeMillis();

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

		// tx info 信息补充
		//TODO  "pIDID":"",                            //<需要冗余>提案的pIDID
		//TODO  "proposalType":"",                    //<需要冗余>提案类型  1:文本提案 2:升级提案 4:取消提案

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}
