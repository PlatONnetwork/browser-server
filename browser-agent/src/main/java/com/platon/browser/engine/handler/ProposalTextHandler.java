package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalExecute;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalTextParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 治理相关(提交文本提案)事件处理类
 * User: dongqile
 * Date: 2019/8/17
 * Time: 20:47
 */
@Component
public class ProposalTextHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalTextHandler.class);


    @Override
    public void handle ( EventContext context ) {
        try {
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            //根据交易参数解析成对应文本提案结构
            CreateProposalTextParam param = tx.getTxParam(CreateProposalTextParam.class);
            CustomProposal customProposal = new CustomProposal();
            customProposal.updateWithProposal(tx);
            //获取配置文件提案参数模板
            String temp = bc.getChainConfig().getProposalUrlTemplate();
            String url = temp.replace(ProposalExecute.key,param.getPIDID());
            //设置url
            customProposal.setUrl(url);
            //从交易解析参数获取需要设置pIDID
            customProposal.setPipId(new Integer(param.getPIDID()));
            //解析器将轮数换成结束块高直接使用
            customProposal.setEndVotingBlock(param.getEndVotingBlock().toString());
            //设置pIDIDNum
            String pIDIDNum = ProposalExecute.pIDIDNum.replace(ProposalExecute.key,param.getPIDID());
            customProposal.setPipNum(pIDIDNum);
            //设置提案类型
            customProposal.setType(String.valueOf(CustomProposal.TypeEnum.TEXT.code));
            //设置提案人
            customProposal.setVerifier(param.getVerifier());
            //设置提案人名称
            customProposal.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            //新增文本提案交易结构
            customProposal.setCanceledPipId(0);
            customProposal.setCanceledTopic("");
            proposalExecuteResult.getAddProposals().add(customProposal);
            //全量数据补充
            bc.PROPOSALS_CACHE.put(customProposal.getPipId().toString(),customProposal);
        }catch (NoSuchBeanException e){
            logger.error("");
        }

    }


}