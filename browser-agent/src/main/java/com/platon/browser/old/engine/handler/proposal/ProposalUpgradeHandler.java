package com.platon.browser.old.engine.handler.proposal;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dto.*;
import com.platon.browser.old.engine.BlockChain;
import com.platon.browser.old.engine.ProposalEngine;
import com.platon.browser.old.engine.cache.CacheHolder;
import com.platon.browser.old.engine.cache.NodeCache;
import com.platon.browser.old.engine.cache.ProposalCache;
import com.platon.browser.old.engine.handler.EventContext;
import com.platon.browser.old.engine.handler.EventHandler;
import com.platon.browser.old.engine.stage.ProposalStage;
import com.platon.browser.old.engine.stage.StakingStage;
import com.platon.browser.old.exception.BusinessException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.CreateProposalUpgradeParam;
import com.platon.browser.util.RoundCalculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @Auther: dongqile
 * @Date: 2019/8/17 20:47
 * @Description: 治理相关(提交文本提案)事件处理类
 */
//@Component
public class ProposalUpgradeHandler implements EventHandler {
    private static Logger logger = LoggerFactory.getLogger(ProposalUpgradeHandler.class);
    @Autowired
    private BlockChain bc;
    @Autowired
    private BlockChainConfig chainConfig;
    @Autowired
    private CacheHolder cacheHolder;

    @Override
    public void handle ( EventContext context ) throws BusinessException {
        NodeCache nodeCache = cacheHolder.getNodeCache();
        ProposalCache proposalCache = cacheHolder.getProposalCache();
        StakingStage stakingStage = cacheHolder.getStageData().getStakingStage();
        ProposalStage proposalStage = cacheHolder.getStageData().getProposalStage();

    	logger.debug("ProposalUpgradeHandler");
        CustomTransaction tx = context.getTransaction();
        //根据交易参数解析成对应文本提案结构
        CreateProposalUpgradeParam param = tx.getTxParam(CreateProposalUpgradeParam.class);
        CustomProposal proposal = new CustomProposal();
        proposal.updateWithCustomTransaction(tx, (long) bc.getCurValidator().size());
        //设置提案人
        proposal.setVerifier(param.getVerifier());

        CustomNode node;
        try {
            node = nodeCache.getNode(param.getVerifier());
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
        proposal.setType(String.valueOf(CustomProposal.TypeEnum.UPGRADE.getCode()));
        //获取配置文件提案参数模板
        String temp = chainConfig.getProposalUrlTemplate();
        String url = temp.replace(ProposalEngine.KEY, param.getPIDID());
        //设置url
        proposal.setUrl(url);
        //从交易解析参数获取需要设置pIDID
        proposal.setPipId(param.getPIDID());
        //解析器将轮数换成结束块高直接使用
        BigDecimal endBlockNumber = RoundCalculation.endBlockNumCal(tx.getBlockNumber().toString(),param.getEndVotingRound(),chainConfig);
        proposal.setEndVotingBlock(endBlockNumber.toString());
        //设置pIDIDNum
        String pIDIDNum = ProposalEngine.PID_ID_NUM.replace(ProposalEngine.KEY, param.getPIDID());
        proposal.setPipNum(pIDIDNum);
        //设置生效时间
        BigDecimal decActiveNumber = RoundCalculation.activeBlockNumCal(tx.getBlockNumber().toString(), param.getEndVotingRound(), chainConfig);
        proposal.setActiveBlock(decActiveNumber.toString());
        //设置新版本号
        proposal.setNewVersion(String.valueOf(param.getNewVersion()));
        proposal.setCanceledPipId("");
        proposal.setCanceledTopic("");
        //新增文本提案交易结构
        proposalStage.insertProposal(proposal);
        //全量数据补充
        proposalCache.addProposal(proposal);

        // 记录操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(), CustomNodeOpt.TypeEnum.PROPOSALS);
        nodeOpt.updateWithCustomTransaction(tx);
        String desc = CustomNodeOpt.TypeEnum.PROPOSALS.getTpl()
                .replace("ID",proposal.getPipId()==null?"":proposal.getPipId())
                .replace("TITLE",proposal.getTopic()==null?"":proposal.getTopic())
                .replace("TYPE",CustomProposal.TypeEnum.UPGRADE.getCode())
                .replace("VERSION",proposal.getNewVersion()==null?"":proposal.getNewVersion());
        nodeOpt.setDesc(desc);
        stakingStage.insertNodeOpt(nodeOpt);
    }
}
