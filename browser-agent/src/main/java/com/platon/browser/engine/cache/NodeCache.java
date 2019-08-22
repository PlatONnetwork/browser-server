package com.platon.browser.engine.cache;

import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomUnDelegation;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 节点进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/17 18:03
 * @Description:
 */
public class NodeCache {
    private static Logger logger = LoggerFactory.getLogger(NodeCache.class);
    // <节点ID - 节点实体>
    private Map<String, CustomNode> nodeMap = new TreeMap<>();
    private Set<CustomStaking> stakingSet = new HashSet<>();
    private Set<CustomDelegation> delegationSet = new HashSet<>();

    public void init(List<CustomNode> nodeList,
                     List<CustomStaking> stakingList,
                     List<CustomDelegation> delegationList,
                     List<CustomUnDelegation> unDelegationList
    ) throws CacheConstructException {
        nodeList.forEach(node->nodeMap.put(node.getNodeId(),node));
        for (CustomStaking staking:stakingList){
            try {
                addStaking(staking);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException("构造缓存错误:"+e.getMessage()+", 无法向其关联质押(stakingBlockNumber="+staking.getStakingBlockNum()+")");
            }
        }
        for (CustomDelegation delegation:delegationList){
            try {
                addDelegation(delegation);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException("构造缓存错误:"+e.getMessage()+", 无法向其关联委托(stakingBlockNumber="+delegation.getStakingBlockNum()+")");
            }
        }
        for (CustomUnDelegation unDelegation:unDelegationList){
            try {
                addUnDelegation(unDelegation);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException("构造缓存错误:"+e.getMessage()+", 无法向其关联解委托(stakingBlockNumber="+unDelegation.getStakingBlockNum()+")");
            }
        }
    }

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

    /**
     * 添加节点，同时更新stakingSet和delegationSet全量缓存
     * @param node
     */
    public void addNode(CustomNode node){
        nodeMap.put(node.getNodeId(),node);
        node.getStakings().forEach((stakingBlockNumber,staking)->{
            stakingSet.add(staking);
            staking.getDelegations().forEach((delegationAddr,delegation)->delegationSet.add(delegation));
        });
    }

    /**
     * 添加委托，同时更新delegationSet全量缓存
     * @param staking
     */
    public void addStaking(CustomStaking staking) throws NoSuchBeanException {
        getNode(staking.getNodeId()).getStakings().put(staking.getStakingBlockNum(),staking);
        stakingSet.add(staking);
        staking.getDelegations().forEach((delegationAddr,delegation)->delegationSet.add(delegation));
    }

    /**
     * 添加委托, 同时更新delegationSet全量缓存
     * @param delegation
     */
    public void addDelegation(CustomDelegation delegation) throws NoSuchBeanException {
        getNode(delegation.getNodeId())
                .getStakings()
                .get(delegation.getStakingBlockNum())
                .getDelegations()
                .put(delegation.getDelegateAddr(),delegation);
        delegationSet.add(delegation);
    }

    /**
     * 添加解委托
     * @param unDelegation
     */
    public void addUnDelegation(CustomUnDelegation unDelegation) throws NoSuchBeanException {
        getNode(unDelegation.getNodeId())
                .getStakings()
                .get(unDelegation.getStakingBlockNum())
                .getDelegations()
                .get(unDelegation.getDelegateAddr())
                .getUnDelegations().add(unDelegation);
    }

    public Collection<CustomNode> getAllNode(){
        return nodeMap.values();
    }

    public Set<CustomStaking> getAllStaking(){
        return stakingSet;
    }

    public Set<CustomDelegation> getAllDelegation(){
        return delegationSet;
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


    /**
     * 缓存维护方法
     * 清扫全量缓存，移除历史数据
     */
    public void sweep(){
        /************清楚质押记录***********/
        // 取所有已退出状态质押记录
        List<CustomStaking> exitedStakingList = getStakingByStatus(Collections.singletonList(CustomStaking.StatusEnum.EXITED));
        // 用于记录无效的质押记录（其所有委托均已变成历史）
        List<CustomStaking> invalidCache = new ArrayList<>();
        exitedStakingList.forEach(staking -> {
            boolean valid = false;
            for(CustomDelegation delegation:staking.getDelegations().values()){
                if(CustomDelegation.YesNoEnum.NO.code==delegation.getIsHistory()){
                    // 只要有一条委托是非历史状态，则它所属的质押记录就不能从缓存中删除，标记其为有效
                    valid = true;
                    break;
                }
            }
            if(!valid) invalidCache.add(staking);
        });
        invalidCache.forEach(staking -> {
            nodeMap.values().forEach(node->node.getStakings().remove(staking.getStakingBlockNum()));
            delegationSet.removeAll(staking.getDelegations().values());
        });
        stakingSet.removeAll(invalidCache);
    }


}
