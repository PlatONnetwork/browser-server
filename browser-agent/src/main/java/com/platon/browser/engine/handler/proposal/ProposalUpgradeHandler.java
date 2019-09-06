package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
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
import com.platon.browser.param.CreateProposalUpgradeParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.platon.browser.engine.BlockChain.NODE_CACHE;
import static com.platon.browser.engine.BlockChain.PROPOSALS_CACHE;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 治理相关(提交文本提案)事件处理类
 */
@Component
public class ProposalUpgradeHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpgradeHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Override
    public void handle ( EventContext context ) throws BusinessException {
        CustomTransaction tx = context.getTransaction();
        ProposalStage proposalStage = context.getProposalStage();
        //根据交易参数解析成对应文本提案结构
        CreateProposalUpgradeParam param = tx.getTxParam(CreateProposalUpgradeParam.class);
        CustomProposal proposal = new CustomProposal();
        proposal.updateWithCustomTransaction(tx,Long.valueOf(bc.getCurValidator().size()));
        //设置提案人
        proposal.setVerifier(param.getVerifier());

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
        proposal.setVerifierName(staking.getStakingName());
        //交易信息回填
        param.setNodeName(staking.getStakingName());
        tx.setTxInfo(JSON.toJSONString(param));

        //设置提案为升级类型
        proposal.setType(String.valueOf(CustomProposal.TypeEnum.UPGRADE.code));
        //获取配置文件提案参数模板
        String temp = chainConfig.getProposalUrlTemplate();
        String url = temp.replace(ProposalEngine.key, param.getPIDID());
        //设置url
        proposal.setUrl(url);
        //从交易解析参数获取需要设置pIDID
        proposal.setPipId(new Integer(param.getPIDID()));
        //解析器将轮数换成结束块高直接使用
        BigDecimal endBlockNumber = RoundCalculation.endBlockNumCal(tx.getBlockNumber().toString(),param.getEndVotingRound().toString(),bc.getChainConfig());
        proposal.setEndVotingBlock(endBlockNumber.toString());
        //设置pIDIDNum
        String pIDIDNum = ProposalEngine.pIDIDNum.replace(ProposalEngine.key, param.getPIDID());
        proposal.setPipNum(pIDIDNum);
        //设置生效时间
        BigDecimal decActiveNumber = RoundCalculation.activeBlockNumCal(tx.getBlockNumber().toString(), param.getEndVotingRound().toString(), bc.getChainConfig());
        proposal.setActiveBlock(decActiveNumber.toString());
        //设置新版本号
        proposal.setNewVersion(String.valueOf(param.getNewVersion()));
        proposal.setCanceledPipId("");
        proposal.setCanceledTopic("");
        //新增文本提案交易结构
        proposalStage.insertProposal(proposal);
        //全量数据补充
        PROPOSALS_CACHE.addProposal(proposal);
    }
}
