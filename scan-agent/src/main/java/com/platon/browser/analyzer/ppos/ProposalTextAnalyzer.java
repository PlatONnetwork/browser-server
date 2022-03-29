package com.platon.browser.analyzer.ppos;

import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.dao.param.ppos.ProposalText;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalTextParam;
import com.platon.browser.utils.RoundCalculation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @description: 委托业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalTextAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private ProposalBusinessMapper proposalBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    /**
     * 提交文本提案(创建提案)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        ProposalTextParam txParam = tx.getTxParam(ProposalTextParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        long startTime = System.currentTimeMillis();

        ProposalText businessParam = ProposalText.builder()
                .nodeId(txParam.getVerifier())
                .pIDID(txParam.getPIDID())
                .url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
                .pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
                .endVotingBlock(RoundCalculation.endBlockNumCal(tx.getNum().toString(), chainConfig.getProposalTextConsensusRounds(), chainConfig).toBigInteger())
                .topic(CustomProposal.QUERY_FLAG)
                .description(CustomProposal.QUERY_FLAG)
                .txHash(tx.getHash())
                .blockNumber(BigInteger.valueOf(tx.getNum()))
                .timestamp(tx.getTime())
                .stakingName(txParam.getNodeName())
                .build();
        proposalBusinessMapper.text(businessParam);


        String desc = NodeOpt.TypeEnum.PROPOSALS.getTpl()
                .replace("ID", txParam.getPIDID())
                .replace("TITLE", businessParam.getTopic())
                .replace("TYPE", String.valueOf(CustomProposal.TypeEnum.TEXT.getCode()))
                .replace("VERSION", "");

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getVerifier());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.PROPOSALS.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(event.getBlock().getNum());
        nodeOpt.setTime(event.getBlock().getTime());

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOpt;
    }

}
