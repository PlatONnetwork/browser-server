package com.platon.browser.engine.handler;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.dto.CustomVote;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.VotingProposalParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * User: dongqile
 * Date: 2019/8/19
 * Time: 10:39
 */
@Component
public class VotingProposalHandler implements EventHandler {

    private static Logger logger = LoggerFactory.getLogger(VotingProposalHandler.class);

    @Override
    public void handle ( EventContext context ) {
        try{
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            VotingProposalParam param = tx.getTxParam(VotingProposalParam.class);
            logger.debug("投票信息:{}", JSON.toJSONString(param));
            CustomVote customVote = new CustomVote();
            customVote.updateWithVote(tx,param);
            customVote.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            //全量数据回填
            proposalExecuteResult.stageAddVotes(customVote);
            //支持票集合
            if(CustomProposal.OptionEnum.SUPPORT.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.get(param.getProposalId()).getYesList().add(customVote);
            }
            //反对票集合
            if(CustomProposal.OptionEnum.OPPOSITION.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.get(param.getProposalId()).getNoList().add(customVote);
            }
            //弃权票集合
            if(CustomProposal.OptionEnum.ABSTENTION.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.get(param.getProposalId()).getAbstentionList().add(customVote);
            }
        }catch (NoSuchBeanException e){
            logger.error("");
        }


    }
}