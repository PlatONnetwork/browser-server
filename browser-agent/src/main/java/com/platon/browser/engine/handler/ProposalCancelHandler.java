package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalExecute;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.param.CancelProposalParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/8/19
 * Time: 13:45
 */
@Component
public class ProposalCancelHandler implements EventHandler{
    private static Logger logger = LoggerFactory.getLogger(ProposalCancelHandler.class);

    @Override
    public void handle ( EventContext context ) {
        try {
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            CancelProposalParam param = tx.getTxParam(CancelProposalParam.class);
            CustomProposal customProposal = new CustomProposal();
            customProposal.updateWithProposal(tx);
            //设置提案人
            customProposal.setVerifier(param.getVerifier());
            //设置提案人名称
            customProposal.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            //设置提案为升级类型
            customProposal.setType(String.valueOf(CustomProposal.TypeEnum.CANCEL.code));
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
            //设置生效时间
            BigDecimal decActiveNumber = RoundCalculation.activeBlockNumCal(tx.getBlockNumber().toString(),param.getEndVotingBlock().toString(),bc.getChainConfig());
            customProposal.setActiveBlock(decActiveNumber.toString());
            //设置被取消的提案id
            customProposal.setCanceledPipId(new Integer(param.getCanceledProposalID()));
            //全局内存变量中查询对应需要取消的提案主题
            customProposal.setCanceledTopic(bc.PROPOSALS_CACHE.get(param.getPIDID().toString()).getTopic());
            customProposal.setNewVersion("");
            //更新新增提案列表
            proposalExecuteResult.stageAddProposals(customProposal);
            //全量数据补充
            bc.PROPOSALS_CACHE.put(customProposal.getPipId().toString(),customProposal);
        }catch (Exception e){
            logger.error("");
        }

    }
}