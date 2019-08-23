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
    public void handle ( EventContext context ) throws NoSuchBeanException {
        try{
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            VotingProposalParam param = tx.getTxParam(VotingProposalParam.class);
            param.setNodeName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            param.setPIDID(bc.PROPOSALS_CACHE.getProposal(new Integer(param.getProposalId())).getPipId().toString());
            param.setProposalType(bc.PROPOSALS_CACHE.getProposal(new Integer(param.getProposalId())).getType());
            tx.setTxInfo(JSON.toJSONString(param));
            logger.debug("投票信息:{}", JSON.toJSONString(param));
            CustomVote customVote = new CustomVote();
            customVote.updateWithVote(tx,param);
            customVote.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            //全量数据回填
            proposalExecuteResult.stageAddVotes(customVote);
            //支持票集合
            if(CustomProposal.OptionEnum.SUPPORT.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.getProposal(Integer.valueOf(param.getProposalId())).getYesList().add(customVote);
            }
            //反对票集合
            if(CustomProposal.OptionEnum.OPPOSITION.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.getProposal(Integer.valueOf(param.getProposalId())).getNoList().add(customVote);
            }
            //弃权票集合
            if(CustomProposal.OptionEnum.ABSTENTION.code == Integer.valueOf(customVote.getOption())){
                bc.PROPOSALS_CACHE.getProposal(Integer.valueOf(param.getProposalId())).getAbstentionList().add(customVote);
            }
        }catch (NoSuchBeanException e){
            throw new NoSuchBeanException("缓存中找不到对应的投票提案:"+e.getMessage());
        }


    }
}
