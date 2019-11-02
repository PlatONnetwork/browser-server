package com.platon.browser.complement.cache;

import com.platon.browser.dto.CustomNode;
import com.platon.browser.exception.NoSuchBeanException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NodeCache {
    private static final Map<String,NodeItem> cache = new HashMap<>();

    /**
     * 根据节点ID获取节点
     * @param nodeId
     * @return
     * @throws NoSuchBeanException
     */
    public NodeItem getNode(String nodeId) throws NoSuchBeanException {
        NodeItem node = cache.get(nodeId);
        if(node==null) throw new NoSuchBeanException("节点(id="+nodeId+")的节点不存在");
        return node;
    }

    /**
     * 添加节点
     * @param node
     */
    public void addNode(NodeItem node){
        cache.put(node.getNodeId(),node);
    }
}
