package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalEngine;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.engine.stage.StakingStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalTextParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 治理相关(提交文本提案)事件处理类
 */
@Component
public class ProposalTextHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalTextHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private CacheHolder cacheHolder;
    @Override
    public void handle ( EventContext context ) throws BusinessException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        ProposalStage proposalStage = cacheHolder.getStageData().getProposalStage();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
    	logger.debug("ProposalTextHandler");
        CustomTransaction tx = context.getTransaction();

        //根据交易参数解析成对应文本提案结构
        CreateProposalTextParam param = tx.getTxParam(CreateProposalTextParam.class);
        CustomProposal proposal = new CustomProposal();
        proposal.updateWithCustomTransaction(tx, (long) bc.getCurValidator().size());
        CustomNode node;
        try {
            node = nodeCache.getNode(param.getVerifier());
        } catch (NoSuchBeanException e) {
            throw new BusinessException("处理文本提案出错:"+e.getMessage());
        }
        CustomStaking staking;
        try {
            staking = node.getLatestStaking();
        } catch (NoSuchBeanException e) {
            throw new BusinessException("处理文本提案出错:"+e.getMessage());
        }
        //交易信息回填
        param.setNodeName(staking.getStakingName());
        tx.setTxInfo(JSON.toJSONString(param));
        //获取配置文件提案参数模板
        String temp = chainConfig.getProposalUrlTemplate();
        String url = temp.replace(ProposalEngine.KEY,param.getPIDID());
        //设置本轮参与人数
        //设置url
        proposal.setUrl(url);
        //从交易解析参数获取需要设置pIDID
        proposal.setPipId(param.getPIDID());
        //解析器将轮数换成结束块高直接使用
        BigDecimal endBlockNumber = RoundCalculation.endBlockNumCal(tx.getBlockNumber().toString(),chainConfig.getProposalTextConsensusRounds(),chainConfig);

        proposal.setEndVotingBlock(endBlockNumber.toString());
        //设置pIDIDNum
        String pIDIDNum = ProposalEngine.PID_ID_NUM.replace(ProposalEngine.KEY,param.getPIDID());
        proposal.setPipNum(pIDIDNum);
        //设置提案类型
        proposal.setType(String.valueOf(CustomProposal.TypeEnum.TEXT.getCode()));
        //设置提案人
        proposal.setVerifier(param.getVerifier());
        //设置提案人名称
        proposal.setVerifierName(staking.getStakingName());
        //新增文本提案交易结构
        proposal.setCanceledPipId("");
        proposal.setCanceledTopic("");
        proposalStage.insertProposal(proposal);
        //全量数据补充
        proposalCache.addProposal(proposal);

        // 记录操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.PROPOSALS);
        nodeOpt.updateWithCustomTransaction(tx);
        String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
                .replace("ID",proposal.getPipId()==null?"":proposal.getPipId())
                .replace("TITLE",proposal.getTopic()==null?"":proposal.getTopic())
                .replace("TYPE",CustomProposal.TypeEnum.TEXT.getCode());
        nodeOpt.setDesc(desc);
        stakingStage.insertNodeOpt(nodeOpt);
    }
}
