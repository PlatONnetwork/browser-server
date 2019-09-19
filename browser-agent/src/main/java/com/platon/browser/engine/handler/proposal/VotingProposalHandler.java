package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.*;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.VotingProposalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 提案投票事件处理类
 */
@Component
public class VotingProposalHandler implements EventHandler {

    private static Logger logger = LoggerFactory.getLogger(VotingProposalHandler.class);

    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle ( EventContext context ) throws NoSuchBeanException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        BlockChainStage stageData = cacheHolder.getStageData();
        ProposalStage proposalStage = cacheHolder.getStageData().getProposalStage();
        CustomTransaction tx = context.getTransaction();
        try{

            VotingProposalParam param = tx.getTxParam(VotingProposalParam.class);

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

            param.setNodeName(staking.getStakingName());

            CustomProposal proposal;
            try {
                proposal = proposalCache.getProposal(param.getProposalId());
            } catch (NoSuchBeanException e) {
                throw new BusinessException("缓存中找不到提案:"+e.getMessage());
            }

            // 交易信息回填
            param.setPIDID(proposal.getPipId().toString());
            param.setProposalType(proposal.getType());
            param.setNodeName(staking.getStakingName());
            param.setUrl(proposal.getUrl());
            tx.setTxInfo(JSON.toJSONString(param));

            String msg = JSON.toJSONString(param);
            logger.debug("投票信息:{}", msg);
            CustomVote vote = new CustomVote();
            vote.updateWithVote(tx,param);
            vote.setVerifierName(staking.getStakingName());
            //全量数据回填
            proposalStage.insertVote(vote);

            proposalCache.addVote(vote);

            // 记录操作日志
            CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.VOTE);
            nodeOpt.updateWithCustomTransaction(tx);
            String desc = CustomNodeOpt.TypeEnum.VOTE.getTpl()
                    .replace("ID",proposal.getPipId().toString())
                    .replace("TITLE",proposal.getTopic())
                    .replace("OPTION",param.getOption());
            nodeOpt.setDesc(desc);
            stageData.getStakingStage().insertNodeOpt(nodeOpt);
        }catch (NoSuchBeanException | BusinessException e){
            throw new NoSuchBeanException("缓存中找不到对应的投票提案:"+e.getMessage());
        }
    }
}
