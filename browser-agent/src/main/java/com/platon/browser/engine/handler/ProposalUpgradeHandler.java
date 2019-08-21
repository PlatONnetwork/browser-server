package com.platon.browser.engine.handler;

import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalExecute;
import com.platon.browser.engine.result.ProposalExecuteResult;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalUpgradeParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2019/8/19
 * Time: 10:38
 */
@Component
public class ProposalUpgradeHandler implements EventHandler{
    private static Logger logger = LoggerFactory.getLogger(ProposalUpgradeHandler.class);


    @Override
    public void handle ( EventContext context ) {
        try {
            CustomTransaction tx = context.getTransaction();
            ProposalExecuteResult proposalExecuteResult = context.getProposalExecuteResult();
            BlockChain bc = context.getBlockChain();
            //根据交易参数解析成对应文本提案结构
            CreateProposalUpgradeParam param = tx.getTxParam(CreateProposalUpgradeParam.class);
            CustomProposal customProposal = new CustomProposal();
            customProposal.updateWithProposal(tx);
            //设置提案人
            customProposal.setVerifier(param.getVerifier());
            //设置提案人名称
            customProposal.setVerifierName(bc.NODE_CACHE.getNode(param.getVerifier()).getLatestStaking().getStakingName());
            //设置提案为升级类型
            customProposal.setType(String.valueOf(CustomProposal.TypeEnum.UPGRADE.code));
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
            //设置新版本号
            customProposal.setNewVersion(String.valueOf(param.getNewVersion()));
            customProposal.setCanceledPipId(0);
            customProposal.setCanceledTopic("");
            //新增文本提案交易结构
            proposalExecuteResult.getAddProposals().add(customProposal);
            //全量数据补充
            bc.PROPOSALS_CACHE.put(customProposal.getPipId().toString(),customProposal);
        }catch (NoSuchBeanException e){
            logger.error("");
        }

    }
}