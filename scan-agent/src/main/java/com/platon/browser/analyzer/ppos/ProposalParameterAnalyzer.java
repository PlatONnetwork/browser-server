package com.platon.browser.analyzer.ppos;

import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.bean.ComplementNodeOpt;
import com.platon.browser.cache.NetworkStatCache;
import com.platon.browser.cache.ProposalCache;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.custommapper.ProposalBusinessMapper;
import com.platon.browser.dao.param.ppos.ProposalParameter;
import com.platon.browser.bean.CustomProposal;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.param.ProposalParameterParam;
import com.platon.browser.service.govern.ParameterService;
import com.platon.browser.utils.RoundCalculation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @description: 参数提案业务参数转换器
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
@Service
public class ProposalParameterAnalyzer extends PPOSAnalyzer<NodeOpt> {

    @Resource
    private BlockChainConfig chainConfig;

    @Resource
    private ProposalBusinessMapper proposalBusinessMapper;

    @Resource
    private NetworkStatCache networkStatCache;

    @Resource
    private ProposalCache proposalCache;

    @Resource
    private ParameterService parameterService;

    /**
     * 提交参数提案(创建提案)
     *
     * @param event
     * @param tx
     * @return com.platon.browser.elasticsearch.dto.NodeOpt
     * @date 2021/6/15
     */
    @Override
    public NodeOpt analyze(CollectionEvent event, Transaction tx) {
        ProposalParameterParam txParam = tx.getTxParam(ProposalParameterParam.class);
        // 补充节点名称
        updateTxInfo(txParam, tx);
        // 失败的交易不分析业务数据
        if (Transaction.StatusEnum.FAILURE.getCode() == tx.getStatus())
            return null;

        long startTime = System.currentTimeMillis();

        BigDecimal voteEndBlockNum = RoundCalculation.getParameterProposalVoteEndBlockNum(tx.getNum(), chainConfig);
        BigDecimal activeBlockNum = voteEndBlockNum.add(BigDecimal.ONE);
        String staleValue = parameterService.getValueInBlockChainConfig(txParam.getName());
        ProposalParameter businessParam = ProposalParameter.builder()
                .nodeId(txParam.getVerifier())
                .pIDID(txParam.getPIDID())
                .url(String.format(chainConfig.getProposalUrlTemplate(), txParam.getPIDID()))
                .pipNum(String.format(chainConfig.getProposalPipNumTemplate(), txParam.getPIDID()))
                .endVotingBlock(voteEndBlockNum.toBigInteger())
                .activeBlock(activeBlockNum.toBigInteger())
                .topic(CustomProposal.QUERY_FLAG)
                .description(CustomProposal.QUERY_FLAG)
                .txHash(tx.getHash())
                .blockNumber(BigInteger.valueOf(tx.getNum()))
                .timestamp(tx.getTime())
                .stakingName(txParam.getNodeName())
                .module(txParam.getModule())
                .name(txParam.getName())
                .staleValue(staleValue)
                .newValue(txParam.getNewValue())
                .build();

        // 业务数据入库
        proposalBusinessMapper.parameter(businessParam);

        // 添加到参数提案缓存Map<未来生效块号,List<提案ID>>
        proposalCache.add(activeBlockNum.longValue(), tx.getHash());

        String desc = NodeOpt.TypeEnum.PARAMETER.getTpl()
                .replace("ID", txParam.getPIDID())
                .replace("TITLE", businessParam.getTopic())
                .replace("TYPE", String.valueOf(CustomProposal.TypeEnum.PARAMETER.getCode()))
                .replace("MODULE", businessParam.getModule())
                .replace("NAME", businessParam.getName())
                .replace("VALUE", businessParam.getNewValue());

        NodeOpt nodeOpt = ComplementNodeOpt.newInstance();
        nodeOpt.setNodeId(txParam.getVerifier());
        nodeOpt.setType(Integer.valueOf(NodeOpt.TypeEnum.PARAMETER.getCode()));
        nodeOpt.setDesc(desc);
        nodeOpt.setTxHash(tx.getHash());
        nodeOpt.setBNum(event.getBlock().getNum());
        nodeOpt.setTime(event.getBlock().getTime());

        log.debug("处理耗时:{} ms", System.currentTimeMillis() - startTime);

        return nodeOpt;
    }

}
