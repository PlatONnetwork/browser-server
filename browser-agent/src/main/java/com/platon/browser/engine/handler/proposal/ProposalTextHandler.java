package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomTransaction;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalEngine;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalTextParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.PROPOSALS_CACHE;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 治理相关(提交文本提案)事件处理类
 */
@Component
public class ProposalTextHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalTextHandler.class);

    @Override
    public void handle ( EventContext context ) throws BusinessException {
        CustomTransaction tx = context.getTransaction();
        ProposalStage proposalStage = context.getProposalStage();
        BlockChain bc = context.getBlockChain();
        //根据交易参数解析成对应文本提案结构
        CreateProposalTextParam param = tx.getTxParam(CreateProposalTextParam.class);
        CustomProposal proposal = new CustomProposal();
        proposal.updateWithCustomTransaction(tx);
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
        //交易信息回填
        param.setNodeName(staking.getStakingName());
        tx.setTxInfo(JSON.toJSONString(param));
        //获取配置文件提案参数模板
        String temp = bc.getChainConfig().getProposalUrlTemplate();
        String url = temp.replace(ProposalEngine.key,param.getPIDID());
        //设置url
        proposal.setUrl(url);
        //从交易解析参数获取需要设置pIDID
        proposal.setPipId(new Integer(param.getPIDID()));
        //解析器将轮数换成结束块高直接使用
        proposal.setEndVotingBlock("");
        //设置pIDIDNum
        String pIDIDNum = ProposalEngine.pIDIDNum.replace(ProposalEngine.key,param.getPIDID());
        proposal.setPipNum(pIDIDNum);
        //设置提案类型
        proposal.setType(String.valueOf(CustomProposal.TypeEnum.TEXT.code));
        //设置提案人
        proposal.setVerifier(param.getVerifier());
        //设置提案人名称
        proposal.setVerifierName(staking.getStakingName());
        //新增文本提案交易结构
        proposal.setCanceledPipId(0);
        proposal.setCanceledTopic("");
        proposalStage.insertProposal(proposal);
        //全量数据补充
        PROPOSALS_CACHE.addProposal(proposal);
    }
}
