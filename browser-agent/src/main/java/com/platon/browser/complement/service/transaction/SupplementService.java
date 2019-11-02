package com.platon.browser.complement.service.transaction;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.complement.cache.NodeCache;
import com.platon.browser.complement.cache.NodeItem;
import com.platon.browser.exception.NoSuchBeanException;
import com.platon.browser.param.DelegateCreateParam;
import com.platon.browser.param.StakeModifyParam;
import com.platon.browser.param.StakeIncreaseParam;
import com.platon.browser.param.DelegateExitParam;
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
    public void supplement(CollectionTransaction tx){
        String nodeId="";
        try {
            NodeItem nodeItem;
            switch (tx.getTypeEnum()) {
                case STAKE_INCREASE: // 增持质押
                    StakeIncreaseParam isp = tx.getTxParam(StakeIncreaseParam.class);
                    nodeId=isp.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(isp.getNodeId());
                        isp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(isp.toJSONString());
                    }
                    break;
                case STAKE_EXIT: // 撤销质押
                    StakeModifyParam evp = tx.getTxParam(StakeModifyParam.class);
                    nodeId=evp.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(evp.getNodeId());
                        evp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(evp.toJSONString());
                    }
                    break;
                case DELEGATE_CREATE: // 发起委托
                    DelegateCreateParam dp = tx.getTxParam(DelegateCreateParam.class);
                    nodeId=dp.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(dp.getNodeId());
                        dp.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(dp.toJSONString());
                    }
                    break;
                case DELEGATE_EXIT: // 撤销委托
                    DelegateExitParam uep = tx.getTxParam(DelegateExitParam.class);
                    nodeId=uep.getNodeId();
                    if(StringUtils.isNotBlank(nodeId)){
                        nodeItem = nodeCache.getNode(uep.getNodeId());
                        uep.setNodeName(nodeItem.getNodeName());
                        tx.setInfo(uep.toJSONString());
                    }
                    break;
                default:
                    break;
            }
        } catch (NoSuchBeanException e) {
            log.error("缓存中找不到交易[{}]参数中的节点ID[{}]对应的节点名称信息!",tx.getHash(),nodeId);
        }
    }
}
