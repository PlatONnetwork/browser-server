package com.platon.browser.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import com.platon.browser.engine.ProposalEngine;
import com.platon.browser.engine.cache.CacheHolder;
import com.platon.browser.engine.cache.NodeCache;
import com.platon.browser.engine.cache.ProposalCache;
import com.platon.browser.engine.handler.EventContext;
import com.platon.browser.engine.handler.EventHandler;
import com.platon.browser.engine.stage.BlockChainStage;
import com.platon.browser.engine.stage.ProposalStage;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CancelProposalParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @Auther: dongqile
 * @Date: 2019/8/19 20:09
 * @Description: 取消提案事件处理类
 */
@Component
public class ProposalCancelHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalCancelHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private CacheHolder cacheHolder;


    @Override
    public void handle ( EventContext context ) throws BusinessException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        BlockChainStage stageData = cacheHolder.getStageData();

    	logger.debug("ProposalCancelHandler Handler");
        CustomTransaction tx = context.getTransaction();
        ProposalStage proposalStage = context.getProposalStage();
        CancelProposalParam param = tx.getTxParam(CancelProposalParam.class);

        CustomNode node;
        try {
            node = nodeCache.getNode(param.getVerifier());
        } catch (NoSuchBeanException e) {
            throw new BusinessException("处理取消提案出错:"+e.getMessage());
        }
        CustomStaking staking;
        try {
            staking = node.getLatestStaking();
        } catch (NoSuchBeanException e) {
            throw new BusinessException("处理取消提案出错:"+e.getMessage());
        }

        // 交易信息回填
        param.setNodeName(staking.getStakingName());
        tx.setTxInfo(JSON.toJSONString(param));

        CustomProposal proposal = new CustomProposal();
        proposal.updateWithCustomTransaction(tx, (long) bc.getCurValidator().size());
        //设置提案人
        proposal.setVerifier(param.getVerifier());
        //设置提案人名称
        proposal.setVerifierName(staking.getStakingName());
        //设置提案为升级类型
        proposal.setType(CustomProposal.TypeEnum.CANCEL.code);
        //获取配置文件提案参数模板
        String temp = bc.getChainConfig().getProposalUrlTemplate();
        String url = temp.replace(ProposalEngine.key,param.getPIDID());
        //设置url
        proposal.setUrl(url);
        //从交易解析参数获取需要设置pIDID
        proposal.setPipId(new Integer(param.getPIDID()));
        //解析器将轮数换成结束块高直接使用
        //结束轮转换结束区块高度
        BigDecimal cancelEndBlockNumber = RoundCalculation.endBlockNumCal(tx.getBlockNumber().toString(),param.getEndVotingRound(),bc.getChainConfig());
        proposal.setEndVotingBlock(cancelEndBlockNumber.toString());
        //设置pIDIDNum
        String pIDIDNum = ProposalEngine.pIDIDNum.replace(ProposalEngine.key,param.getPIDID());
        proposal.setPipNum(pIDIDNum);
        //设置生效时间
        BigDecimal decActiveNumber = RoundCalculation.activeBlockNumCal(tx.getBlockNumber().toString(),param.getEndVotingRound(),bc.getChainConfig());
        proposal.setActiveBlock(decActiveNumber.toString());
        //设置被取消的提案id
        proposal.setCanceledPipId(param.getCanceledProposalID());
        //全局内存变量中查询对应需要取消的提案主题
        CustomProposal targetProposal;
        try {
            targetProposal = proposalCache.getProposal(param.getCanceledProposalID());
        } catch (NoSuchBeanException e) {
            throw new BusinessException("缓存中不存在被取消的目标提案:"+e.getMessage());
        }
        proposal.setCanceledTopic(targetProposal.getTopic());
        proposal.setNewVersion("");
        //更新新增提案列表
        proposalStage.insertProposal(proposal);
        //全量数据补充
        proposalCache.addProposal(proposal);

        // 记录操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.PROPOSALS);
        nodeOpt.updateWithCustomTransaction(tx);
        String desc = CustomNodeOpt.TypeEnum.PROPOSALS.tpl
                .replace("ID",proposal.getPipId().toString())
                .replace("TITLE",proposal.getTopic())
                .replace("TYPE",CustomProposal.TypeEnum.TEXT.code);
        nodeOpt.setDesc(desc);
        stageData.getStakingStage().insertNodeOpt(nodeOpt);
    }
}
