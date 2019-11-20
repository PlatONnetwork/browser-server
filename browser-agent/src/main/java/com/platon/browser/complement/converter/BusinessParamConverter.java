package com.platon.browser.complement.converter;

import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.common.queue.collection.event.CollectionEvent;
import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.enums.InnerContractAddrEnum;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

/**
 * @description: 业务参数转换器基类
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
@Slf4j
public abstract class BusinessParamConverter<T> {

    @Autowired
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

    public abstract T convert(CollectionEvent event, Transaction tx) throws NoSuchBeanException;
}
