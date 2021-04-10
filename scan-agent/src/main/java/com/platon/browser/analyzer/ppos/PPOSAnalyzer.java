package com.platon.browser.analyzer.ppos;

import com.platon.browser.cache.NodeCache;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.bean.CollectionEvent;
import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.BlockNumberException;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * @description: 业务参数转换器基类
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
public abstract class PPOSAnalyzer<T> {

    @Resource
    protected NodeCache nodeCache;

    protected NodeItem updateNodeCache(String nodeId,String nodeName){
        NodeItem nodeItem;
        try {
            nodeItem = nodeCache.getNode(nodeId);
            nodeItem.setNodeName(StringUtils.isBlank(nodeName)?nodeItem.getNodeName():nodeName);
        } catch (NoSuchBeanException e) {
            nodeItem = NodeItem.builder().nodeId(nodeId).nodeName(nodeName).build();
            nodeCache.addNode(nodeItem);
        }
        return nodeItem;
    }
    
    protected void updateNodeCache(String nodeId,String nodeName, BigInteger stakingBlockNum){
        NodeItem nodeItem = updateNodeCache(nodeId,nodeName);
        nodeItem.setStakingBlockNum(stakingBlockNum);
    }

    protected int isInit(String benefitAddress){
    	return InnerContractAddrEnum.INCENTIVE_POOL_CONTRACT.getAddress().equalsIgnoreCase(benefitAddress)?
    			BusinessParam.YesNoEnum.YES.getCode()
    			:BusinessParam.YesNoEnum.NO.getCode();
    }

    public abstract T analyze(CollectionEvent event, Transaction tx) throws NoSuchBeanException, BlockNumberException;


    protected void updateTxInfo(TxParam txParam,Transaction tx){
        if(txParam==null) return;
        NodeItem nodeItem;
        try {
            switch (tx.getTypeEnum()) {
                case STAKE_CREATE: // 1000 创建验证人
                    break;
                case STAKE_MODIFY: // 1001 编辑验证人
                    break;
                case STAKE_INCREASE: // 1002 增持质押
                    StakeIncreaseParam sip = (StakeIncreaseParam)txParam;
                    nodeItem = nodeCache.getNode(sip.getNodeId());
                    sip.setNodeName(nodeItem.getNodeName())
                            .setStakingBlockNum(nodeItem.getStakingBlockNum());
                    break;
                case STAKE_EXIT: // 1003 退出质押
                    StakeExitParam sep = (StakeExitParam)txParam;
                    nodeItem = nodeCache.getNode(sep.getNodeId());
                    sep.setNodeName(nodeItem.getNodeName())
                            .setStakingBlockNum(nodeItem.getStakingBlockNum());
                    break;
                case DELEGATE_CREATE: // 1004
                    DelegateCreateParam dcp = (DelegateCreateParam)txParam;
                    nodeItem = nodeCache.getNode(dcp.getNodeId());
                    dcp.setNodeName(nodeItem.getNodeName()).setStakingBlockNum(nodeItem.getStakingBlockNum());
                    break;
                case DELEGATE_EXIT: // 1005
                    DelegateExitParam dep = (DelegateExitParam)txParam;
                    nodeItem = nodeCache.getNode(dep.getNodeId());
                    dep.setNodeName(nodeItem.getNodeName());
                    break;
                case PROPOSAL_TEXT: // 2000
                    ProposalTextParam ptp = (ProposalTextParam)txParam;
                    nodeItem = nodeCache.getNode(ptp.getVerifier());
                    ptp.setNodeName(nodeItem.getNodeName());
                    break;
                case PROPOSAL_UPGRADE: // 2001
                    ProposalUpgradeParam pup = (ProposalUpgradeParam)txParam;
                    nodeItem = nodeCache.getNode(pup.getVerifier());
                    pup.setNodeName(nodeItem.getNodeName());
                    break;
                case PROPOSAL_PARAMETER: // 2002
                    ProposalParameterParam ppp = (ProposalParameterParam)txParam;
                    nodeItem = nodeCache.getNode(ppp.getVerifier());
                    ppp.setNodeName(nodeItem.getNodeName());
                    break;
                case PROPOSAL_CANCEL: // 2005
                    ProposalCancelParam pcp = (ProposalCancelParam)txParam;
                    nodeItem = nodeCache.getNode(pcp.getVerifier());
                    pcp.setNodeName(nodeItem.getNodeName());
                    break;
                case PROPOSAL_VOTE: // 2003
                    ProposalVoteParam pvp = (ProposalVoteParam)txParam;
                    nodeItem = nodeCache.getNode(pvp.getVerifier());
                    pvp.setNodeName(nodeItem.getNodeName());
                    break;
                case VERSION_DECLARE: // 2004
                    VersionDeclareParam vdp = (VersionDeclareParam)txParam;
                    nodeItem = nodeCache.getNode(vdp.getActiveNode());
                    vdp.setNodeName(nodeItem.getNodeName());
                    break;
                case REPORT: // 3000
                    ReportParam rp = (ReportParam)txParam;
                    nodeItem = nodeCache.getNode(rp.getVerify());
                    rp.setNodeName(nodeItem.getNodeName())
                            .setStakingBlockNum(nodeItem.getStakingBlockNum());
                    break;
                case RESTRICTING_CREATE: // 4000
                    break;
                case CLAIM_REWARDS: // 5000
                    // 把交易回执里的领取奖励数量设置到TxInfo,
                    DelegateRewardClaimParam drcp = (DelegateRewardClaimParam)txParam;
                    drcp.getRewardList().forEach(reward -> {
                        String nodeName = "Unknown";
                        try {
                            NodeItem node = nodeCache.getNode(reward.getNodeId());
                            nodeName=node.getNodeName();
                        } catch (NoSuchBeanException e) {
                            log.error("{}",e.getMessage());
                        }
                        reward.setNodeName(nodeName);
                    });
                    break;
                default:
                    break;
            }
            tx.setInfo(txParam.toJSONString());
        }catch (NoSuchBeanException e){
            log.warn("缓存中找不到节点信息,无法补节点名称和质押区块号:{}",txParam.toJSONString());
        }
    }
}
