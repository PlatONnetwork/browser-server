package com.platon.browser.cache;

import cn.hutool.core.collection.CollUtil;
import com.platon.browser.bean.NodeItem;
import com.platon.browser.dao.entity.Node;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节点缓存
 */
@Slf4j
@Component
public class NodeCache {

    private static final Map<String, NodeItem> cache = new HashMap<>();

    /**
     * 清除节点缓存
     *
     * @param :
     * @return: void
     * @date: 2021/11/30
     */
    public void cleanNodeCache() {
        cache.clear();
    }

    /**
     * 根据节点ID获取节点
     *
     * @param nodeId
     * @return
     * @throws NoSuchBeanException
     */
    public NodeItem getNode(String nodeId) throws NoSuchBeanException {
        NodeItem node = cache.get(nodeId);
        if (node == null) throw new NoSuchBeanException("节点(id=" + nodeId + ")的节点不存在");
        return node;
    }

    /**
     * 添加节点
     *
     * @param node
     */
    public void addNode(NodeItem node) {
        cache.put(node.getNodeId(), node);
    }

    /**
     * 初始化节点缓存
     *
     * @param nodeList
     * @return void
     * @date 2021/4/19
     */
    public void init(List<Node> nodeList) {
        log.info("初始化节点缓存");
        if (nodeList.isEmpty()) return;
        nodeList.forEach(s -> {
            NodeItem node = NodeItem.builder()
                                    .nodeId(s.getNodeId())
                                    .nodeName(s.getNodeName())
                                    .stakingBlockNum(BigInteger.valueOf(s.getStakingBlockNum()))
                                    .nodeSettleStatisInfo(s.getNodeSettleStatisInfo())
                                    .build();
            addNode(node);
        });
    }

    /**
     * 转成list---节点周期出块信息统计
     *
     * @param
     * @return java.util.List<com.platon.browser.dao.entity.Node>
     * @date 2021/6/2
     */
    public List<Node> toNodeSettleStatisInfoList() {
        List<Node> list = CollUtil.newArrayList();
        cache.forEach((k, v) -> {
            Node node = new Node();
            node.setNodeId(k);
            node.setNodeSettleStatisInfo(v.getNodeSettleStatisInfo());
            list.add(node);
        });
        return list;
    }

}
