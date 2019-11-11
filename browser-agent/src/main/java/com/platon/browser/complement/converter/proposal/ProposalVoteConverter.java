package com.platon.browser.complement.converter.proposal;

import com.platon.browser.common.complement.cache.NetworkStatCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.complement.dto.ComplementNodeOpt;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.converter.BusinessParamConverter;
import com.platon.browser.complement.dao.mapper.ProposalBusinessMapper;
import com.platon.browser.complement.dao.param.proposal.ProposalVote;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalVoteParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
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
    @Autowired
    private ProposalMapper proposalMapper;


    @Override
    public Optional<NodeOpt> convert(CollectionEvent event, Transaction tx) throws Exception {
        ProposalVoteParam txParam = tx.getTxParam(ProposalVoteParam.class);
        String nodeId=txParam.getVerifier();
        NodeItem nodeItem = nodeCache.getNode(nodeId);
        txParam.setNodeName(nodeItem.getNodeName());
        tx.setInfo(txParam.toJSONString());
        // 失败的交易不分析业务数据
        if(Transaction.StatusEnum.FAILURE.getCode()==tx.getStatus()) return Optional.ofNullable(null);

        long startTime = System.currentTimeMillis();

		// 获得参数
        String proposalId = txParam.getProposalId();
        String nodeName = nodeItem.getNodeName();
        String txHash = tx.getHash();
        Long blockNum = event.getBlock().getNum();
        Date time = tx.getTime();

        // 查询投票的提案信息
        Proposal proposal = proposalMapper.selectByPrimaryKey(proposalId);

        // 投票记录
    	ProposalVote businessParam= ProposalVote.builder()
    			.nodeId(nodeId)
    			.txHash(txHash)
    			.bNum(BigInteger.valueOf(blockNum))
    			.timestamp(time)
    			.stakingName(nodeName)
    			.proposalHash(txParam.getProposalId())
    			.voteOption(Integer.valueOf(txParam.getOption()))
                .build();

    	proposalBusinessMapper.vote(businessParam);

		String desc = NodeOpt.TypeEnum.VOTE.getTpl()
				.replace("ID",proposal.getPipId())
				.replace("TITLE",proposal.getTopic())
				.replace("OPTION",txParam.getOption())
				.replace("TYPE", String.valueOf(proposal.getType()))
				.replace("VERSION",proposal.getNewVersion()==null?"":proposal.getNewVersion());

		NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
		nodeOpt.setId(networkStatCache.getAndIncrementNodeOptSeq());
		nodeOpt.setNodeId(nodeId);
		nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.VOTE.getCode()));
		nodeOpt.setDesc(desc);
		nodeOpt.setTxHash(txHash);
		nodeOpt.setBNum(blockNum);
		nodeOpt.setTime(time);

        // 补充txInfo
        txParam.setPIDID(proposal.getPipId());
        txParam.setProposalType(String.valueOf(proposal.getType()));
        tx.setInfo(txParam.toJSONString());

		log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);

        return Optional.ofNullable(nodeOpt);
    }
}
