package com.platon.browser.complement.service.param.converter;

import com.platon.browser.common.collection.dto.CollectionTransaction;
import com.platon.browser.common.complement.cache.NodeCache;
import com.platon.browser.common.complement.cache.bean.NodeItem;
import com.platon.browser.exception.NoSuchBeanException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description: 业务参数转换器基类
 * @author: chendongming@juzix.net
 * @create: 2019-11-04 17:58:27
 **/
public abstract class BusinessParamConverter<T> {

    @Autowired
    private NodeCache nodeCache;

    protected void updateNodeCache(String nodeId,String nodeName){
        try {
            NodeItem nodeItem = nodeCache.getNode(nodeId);
            nodeItem.setNodeName(StringUtils.isBlank(nodeName)?nodeItem.getNodeName():nodeName);
        } catch (NoSuchBeanException e) {
            NodeItem nodeItem = NodeItem.builder().nodeId(nodeId).nodeName(nodeName).build();
            nodeCache.addNode(nodeItem);
        }
    }

    abstract T convert(CollectionTransaction tx);
}
