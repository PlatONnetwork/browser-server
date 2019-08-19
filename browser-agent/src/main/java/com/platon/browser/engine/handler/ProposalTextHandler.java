package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalExecute;
import com.platon.browser.engine.StakingExecute;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalTextParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProposalExecute proposalExecute;

    @Override
    public void handle ( EventContext context ) {
        try {
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            //根据交易参数解析成对应文本提案结构
            CreateProposalTextParam param = tx.getTxParam(CreateProposalTextParam.class);
            CustomProposal customProposal = new CustomProposal();
            //设置提案发起人所属节点nodeName
            customProposal.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            customProposal.updateWithProposalText(tx,param);
            //新增文本提案交易结构
            proposalExecuteResult.getAddProposals().add(customProposal);
            //全量数据补充
            proposalExecute.getProposals().put(tx.getFrom(),customProposal);
        }catch (NoSuchBeanException e){
            logger.error("");
        }

    }
}