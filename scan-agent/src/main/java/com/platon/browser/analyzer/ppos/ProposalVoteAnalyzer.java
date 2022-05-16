package com.platon.browser.analyzer.ppos;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.mapper.ProposalMapper;
import com.platon.browser.dao.param.ppos.ProposalVote;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.param.ProposalVoteParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalVoteAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private ProposalBusinessMapper proposalBusinessMapper;

    @Resource
    private ProposalMapper proposalMapper;

    /**
     * 给提案投票(提案投票)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        ProposalVoteParam txParam = tx.getTxParam(ProposalVoteParam.class);

        String proposalId = txParam.getProposalId();
        // 查询投票的提案信息
        Proposal proposal = null;
        try {
            proposal = proposalMapper.selectByPrimaryKey(proposalId);
            txParam.setPIDID(proposal.getPipId());
            txParam.setProposalType(String.valueOf(proposal.getType()));
        } catch (Exception e) {
            log.error(StrUtil.format("查询提案[{}]异常", proposalId), e);
            throw e;
        }

        if (ObjectUtil.isNull(proposal)) {
            throw new BusinessException("找不到投票提案[proposalId=" + txParam.getProposalId() + "], 无法相关信息!");
        }

        // 补充节点名称
        updateTxInfo(txParam, tx);

        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus()) {
            return null;
        }

        long startTime = System.currentTimeMillis();

        // 获得参数
        String nodeName = txParam.getNodeName();
        String txHash = tx.getHash();
        Long blockNum = event.getBlock().getNum();
        Date time = tx.getTime();

        // 投票记录
        ProposalVote businessParam = ProposalVote.builder()
                                                 .nodeId(txParam.getVerifier())
                                                 .txHash(txHash)
                                                 .bNum(BigInteger.valueOf(blockNum))
                                                 .timestamp(time)
                                                 .stakingName(nodeName)
                                                 .proposalHash(txParam.getProposalId())
                                                 .voteOption(Integer.valueOf(txParam.getOption()))
                                                 .build();

        proposalBusinessMapper.vote(businessParam);

        String desc = NodeOpt.TypeEnum.VOTE.getTpl()
                                           .replace("ID", proposal.getPipId())
                                           .replace("TITLE", proposal.getTopic())
                                           .replace("OPTION", txParam.getOption())
                                           .replace("TYPE", String.valueOf(proposal.getType()))
                                           .replace("VERSION", proposal.getNewVersion() == null ? "" : proposal.getNewVersion());

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getVerifier());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.VOTE.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(txHash);
        nodeOpt.setBNum(blockNum);
        nodeOpt.setTime(time);

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOpt;
    }

}
