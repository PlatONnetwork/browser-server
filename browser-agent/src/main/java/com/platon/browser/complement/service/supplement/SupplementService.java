package com.platon.browser.complement.service.supplement;

import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 缺失信息补充服务
 */
@Slf4j
@Service
public class SupplementService {
    @Autowired
    private NodeCache nodeCache;

    /**
     * 补充节点相关信息
     * @param tx
     */
    public void supplement(Transaction tx){
        long startTime = System.currentTimeMillis();

        String nodeId="";
        try {
            NodeItem nodeItem;
            switch (tx.getTypeEnum()) {
                case STAKE_INCREASE: // 增持质押
                    StakeIncreaseParam isp = tx.getTxParam(StakeIncreaseParam.class);
                    nodeId=isp.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        isp.setNodeName(nodeItem.getNodeName());
                        isp.setStakingBlockNum(nodeItem.getStakingBlockNum());
                        tx.setInfo(isp.toJSONString());
                    }
                    break;
                case STAKE_EXIT: // 撤销质押
                	// 迁移到 com.platon.browser.complement.converter.stake.StakeExitConverter.convert(CollectionEvent, Transaction)
                    break;
                case DELEGATE_CREATE: // 发起委托
                    DelegateCreateParam dp = tx.getTxParam(DelegateCreateParam.class);
                    nodeId=dp.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        dp.setNodeName(nodeItem.getNodeName());
                        dp.setStakingBlockNum(nodeItem.getStakingBlockNum());
                        tx.setInfo(dp.toJSONString());
                    }
                    break;
                case DELEGATE_EXIT: // 撤销委托
                    DelegateExitParam uep = tx.getTxParam(DelegateExitParam.class);
                    nodeId=uep.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        uep.setNodeName(nodeItem.getNodeName());
                        //TODO "realAmount":""                   //<需要冗余>真正减持的金额
                        tx.setInfo(uep.toJSONString());
                    }
                    break;
                case PROPOSAL_TEXT:
                	ProposalTextParam ptp = tx.getTxParam(ProposalTextParam.class);
                    nodeId=ptp.getVerifier();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        ptp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(ptp.toJSONString());
                    }
                    break;
                case PROPOSAL_UPGRADE:
                	ProposalUpgradeParam pup = tx.getTxParam(ProposalUpgradeParam.class);
                    nodeId=pup.getVerifier();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        pup.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(pup.toJSONString());
                    }
                    break;
                case PROPOSAL_CANCEL:
                	ProposalCancelParam pcp = tx.getTxParam(ProposalCancelParam.class);
                    nodeId=pcp.getVerifier();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        pcp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(pcp.toJSONString());
                    }
                    break;
                case PROPOSAL_VOTE:
                	//迁移到 com.platon.browser.complement.converter.proposal.ProposalVoteConverter.convert(CollectionEvent, Transaction)
                    break;
                case VERSION_DECLARE:
                	VersionDeclareParam vdp = tx.getTxParam(VersionDeclareParam.class);
                    nodeId=vdp.getActiveNode();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        vdp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(vdp.toJSONString());
                    }
                    break;
                case REPORT:
                	ReportParam rp = tx.getTxParam(ReportParam.class);
                    nodeId=rp.getVerify();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(nodeId);
                        rp.setNodeName(nodeItem.getNodeName());
                        rp.setStakingBlockNum(nodeItem.getStakingBlockNum());
                        tx.setInfo(rp.toJSONString());
                    }
                    break;
                default:
                    break;
            }
        } catch (NoSuchBeanException e) {
            log.error("缓存中找不到交易[{}]参数中的节点ID[{}]对应的节点名称信息!",tx.getHash(),nodeId);
        }
        log.debug("处理耗时:{} ms",System.currentTimeMillis()-startTime);
    }
}
