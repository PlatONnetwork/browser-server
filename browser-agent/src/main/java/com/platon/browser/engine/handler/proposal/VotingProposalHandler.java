package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.*;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.VotingProposalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.PROPOSALS_CACHE;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 提案投票事件处理类
 */
@Component
public class VotingProposalHandler implements EventHandler {

    private static Logger logger = LoggerFactory.getLogger(VotingProposalHandler.class);
    @Override
    public void handle ( EventContext context ) throws NoSuchBeanException {
        try{
            CustomTransaction tx = context.getTransaction();
            ProposalStage proposalStage = context.getProposalStage();
            VotingProposalParam param = tx.getTxParam(VotingProposalParam.class);

            CustomNode node;
            try {
                node = NODE_CACHE.getNode(param.getVerifier());
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
                proposal = PROPOSALS_CACHE.getProposal(param.getProposalId());
            } catch (NoSuchBeanException e) {
                throw new BusinessException("缓存中找不到提案:"+e.getMessage());
            }
            param.setPIDID(proposal.getPipId().toString());
            param.setProposalType(proposal.getType());
            tx.setTxInfo(JSON.toJSONString(param));
            logger.debug("投票信息:{}", JSON.toJSONString(param));
            CustomVote vote = new CustomVote();
            vote.updateWithVote(tx,param);
            vote.setVerifierName(staking.getStakingName());
            //全量数据回填
            proposalStage.insertVote(vote);

            PROPOSALS_CACHE.addVote(vote);
        }catch (NoSuchBeanException | BusinessException e){
            throw new NoSuchBeanException("缓存中找不到对应的投票提案:"+e.getMessage());
        }


    }
}
