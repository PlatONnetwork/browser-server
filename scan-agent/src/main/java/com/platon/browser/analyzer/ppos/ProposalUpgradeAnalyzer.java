package com.platon.browser.analyzer.ppos;

import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.dao.param.ppos.ProposalUpgrade;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalUpgradeParam;
import com.platon.browser.utils.RoundCalculation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 升级提案业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalUpgradeAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private ProposalBusinessMapper proposalBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private ProposalCache proposalCache;

    /**
     * 提交升级提案(创建提案)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        ProposalUpgradeParam txParam = tx.getTxParam(ProposalUpgradeParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        BigInteger voteNum = RoundCalculation.endBlockNumCal(tx.getNum().toString(), txParam.getEndVotingRound(), chainConfig).toBigInteger();
        long startTime = System.currentTimeMillis();
        ProposalUpgrade businessParam = ProposalUpgrade.builder()
                .nodeId(txParam.getVerifier())
                .pIDID(txParam.getPIDID())
                .url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
                .pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
                .endVotingBlock(voteNum)
                .activeBlock(RoundCalculation.activeBlockNumCal(new BigDecimal(voteNum), chainConfig).toBigInteger())
                .topic(CustomProposal.QUERY_FLAG)
                .description(CustomProposal.QUERY_FLAG)
                .txHash(tx.getHash())
                .blockNumber(BigInteger.valueOf(tx.getNum()))
                .timestamp(tx.getTime())
                .stakingName(txParam.getNodeName())
                .newVersion(String.valueOf(txParam.getNewVersion()))
                .build();

        proposalBusinessMapper.upgrade(businessParam);

        // 添加到参数提案缓存Map<未来生效块号,List<提案ID>>
        BigInteger activeBlockNum = businessParam.getActiveBlock();
        proposalCache.add(activeBlockNum.longValue(), tx.getHash());

        String desc = NodeOpt.TypeEnum.PROPOSALS.getTpl()
                .replace("ID", txParam.getPIDID())
                .replace("TITLE", businessParam.getTopic())
                .replace("TYPE", String.valueOf(CustomProposal.TypeEnum.UPGRADE.getCode()))
                .replace("VERSION", businessParam.getNewVersion());

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
