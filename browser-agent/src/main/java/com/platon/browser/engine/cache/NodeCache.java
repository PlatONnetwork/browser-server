package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomUnDelegation;
import com.platon.browser.exception.NoSuchBeanException;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/17 18:03
 * @Description:
 */
public class NodeCache {
    // <节点ID - 节点实体>
    private Map<String, CustomNode> nodeMap = new TreeMap<>();
    /**
     * 根据节点ID获取节点
     * @param nodeId
     * @return
     * @throws NoSuchBeanException
     */
    public CustomNode getNode(String nodeId) throws NoSuchBeanException {
        CustomNode node = nodeMap.get(nodeId);
        if(node==null) throw new NoSuchBeanException("节点(id="+nodeId+")的节点不存在");
        return node;
    }
    public void add(CustomNode node){
        nodeMap.put(node.getNodeId(),node);
    }

    public Map<String, CustomNode> getAll(){
        return nodeMap;
    }
    /**
     * 获取指定状态的所有质押信息
     * @param statuses
     * @return
     */
    public List<CustomStaking> getStakingByStatus(List<CustomStaking.StatusEnum> statuses){
        List<CustomStaking> returnData = new ArrayList<>();
        nodeMap.values().forEach(node->
                node.getStakings().values().stream()
                        .filter(staking -> statuses.contains(CustomStaking.StatusEnum.getEnum(staking.getStatus())))
                        .forEach(staking->returnData.add(staking)));
        return returnData;
    }

    /**
     * 获取指定状态的委托信息
     */
    public List<CustomDelegation> getDelegationByIsHistory(List<CustomDelegation.YesNoEnum> statuses){
        List<CustomDelegation> returnData = new ArrayList<>();
        nodeMap.values().forEach(node->
                node.getStakings().values().forEach(staking ->
                        staking.getDelegations().values().stream().
                                filter(delegation -> statuses.contains(CustomDelegation.YesNoEnum.getEnum(delegation.getIsHistory())))
                                .forEach(delegation -> returnData.add(delegation))));
        return returnData;
    }

    /**
    * 获取指定状态的解委托信息
     */
    public List<CustomUnDelegation> getUnDelegationByStatus(List<CustomUnDelegation.StatusEnum> statuses){
        List<CustomUnDelegation> returnData = new ArrayList<>();
        nodeMap.values().forEach(node->
                node.getStakings().values().forEach(staking ->
                        staking.getDelegations().values().forEach(delegate->
                                delegate.getUnDelegations().stream()
                                        .filter(unDelegation->statuses.contains(CustomUnDelegation.StatusEnum.getEnum(unDelegation.getStatus())))
                                        .forEach(unDelegation -> returnData.add(unDelegation)))));
        return returnData;
    }
}
