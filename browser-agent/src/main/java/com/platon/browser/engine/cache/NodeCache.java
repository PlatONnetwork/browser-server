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
import java.util.concurrent.CopyOnWriteArraySet;

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
    private Set<CustomStaking> stakingSet = new CopyOnWriteArraySet<>();
    private Set<CustomDelegation> delegationSet = new CopyOnWriteArraySet<>();

    public void init(List<CustomNode> nodeList,
                     List<CustomStaking> stakingList,
                     List<CustomDelegation> delegationList,
                     List<CustomUnDelegation> unDelegationList
    ) throws CacheConstructException {
    	logger.debug("NodeCache init" );
        nodeList.forEach(this::addNode);
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
     * 根据节点ID和质押区块号获取质押信息
     * @param nodeId
     * @param stakingNumber
     * @return
     * @throws NoSuchBeanException
     */
    public CustomStaking getStaking(String nodeId,Long stakingNumber) throws NoSuchBeanException {
        CustomNode node = getNode(nodeId);
        CustomStaking staking = node.getStakings().get(stakingNumber);
        if(staking==null) throw new NoSuchBeanException("节点未(id="+nodeId+")且质押区块号为("+stakingNumber+")的质押记录不存在");
        return staking;
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

    /**
     * 获取所有节点
     * @return
     */
    public Collection<CustomNode> getAllNode(){
        return nodeMap.values();
    }

    /**
     * 获取所有质押
     * @return
     */
    public Set<CustomStaking> getAllStaking(){
        return stakingSet;
    }
    /**
     * 获取所有委托
     * @return
     */
    public Set<CustomDelegation> getAllDelegation(){
        return delegationSet;
    }

    /**
     * 获取指定状态的所有质押信息
     * @return
     */
    public List<CustomStaking> getStakingByStatus(CustomStaking.StatusEnum... statusArr){
        List<?> statuses = Arrays.asList(statusArr);
        List<CustomStaking> returnData = new ArrayList<>();
        stakingSet.stream()
                .filter(staking -> statuses.contains(staking.getStatusEnum()))
                .forEach(returnData::add);
        return returnData;
    }

    /**
     * 获取指定状态的委托信息
     */
    public List<CustomDelegation> getDelegationByIsHistory(CustomDelegation.YesNoEnum... statusArr){
        List<?> statuses = Arrays.asList(statusArr);
        List<CustomDelegation> returnData = new ArrayList<>();
        delegationSet.stream()
                .filter(delegation -> statuses.contains(delegation.getIsHistoryEnum()))
                .forEach(returnData::add);
        return returnData;
    }

    /**
    * 获取指定状态的解委托信息
     */
    public List<CustomUnDelegation> getUnDelegationByStatus(CustomUnDelegation.StatusEnum... statusArr){
        List<?> statuses = Arrays.asList(statusArr);
        List<CustomUnDelegation> returnData = new ArrayList<>();
        delegationSet.forEach(delegate->delegate.getUnDelegations().stream()
                .filter(unDelegation->statuses.contains(unDelegation.getStatusEnum()))
                .forEach(returnData::add));
        return returnData;
    }


    /**
     * 缓存维护方法
     * 清扫全量缓存，移除历史数据
     */
    public void sweep(){
        /************清楚质押记录***********/
        // 取所有已退出状态质押记录
        List<CustomStaking> exitedStakingList = getStakingByStatus(CustomStaking.StatusEnum.EXITED);
        // 用于记录无效的质押记录（其所有委托均已变成历史）
        List<CustomStaking> invalidStaking = new ArrayList<>();
        List<CustomDelegation> historyDelegations = new ArrayList<>();
        List<CustomUnDelegation> returnedUnDelegations = new ArrayList<>();
        exitedStakingList.forEach(staking -> {
            boolean isStakingValid = false;
            historyDelegations.clear();
            for(Map.Entry<String,CustomDelegation> delegationEntry:staking.getDelegations().entrySet()){
                CustomDelegation delegation = delegationEntry.getValue();
                if(CustomDelegation.YesNoEnum.NO.getCode()==delegation.getIsHistory()){
                    // 只要有一条委托是非历史状态，则它所属的质押记录就不能从缓存中删除，标记其为有效
                    isStakingValid = true;
                    // 过滤出退回成功的解委托记录
                    returnedUnDelegations.clear();
                    delegation.getUnDelegations().stream()
                            .filter(unDelegation -> CustomUnDelegation.StatusEnum.EXITED.code==unDelegation.getStatus())
                            .forEach(returnedUnDelegations::add);
                    // 解除其下退回成功的解委托关联,释放内存
                    delegation.getUnDelegations().removeAll(returnedUnDelegations);
                } else {
                    // 无效委托记录到历史列表
                    historyDelegations.add(delegation);
                    // 解除其下所有解委托的关联,释放内存
                    delegation.getUnDelegations().removeAll(delegation.getUnDelegations());
                }
            }
            // 删除当前质押下的历史委托
            staking.getDelegations().entrySet().removeAll(historyDelegations);
            // 记录无效质押
            if(!isStakingValid) invalidStaking.add(staking);
        });

        invalidStaking.forEach(staking -> {
            // 清除质押
            CustomNode node = nodeMap.get(staking.getNodeId());
            node.getStakings().remove(staking.getStakingBlockNum());
            // 在所有委托缓存中清除指定实体
            delegationSet.removeAll(staking.getDelegations().values());
        });
        // 在所有质押缓存中清除指定实体
        stakingSet.removeAll(invalidStaking);
    }
}
