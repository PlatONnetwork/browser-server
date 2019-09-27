package com.platon.browser.engine.cache;

import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomNode;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomUnDelegation;
import com.platon.browser.exception.CacheConstructException;
import com.platon.browser.exception.NoSuchBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * 节点进程缓存
 * @Auther: Chendongming
 * @Date: 2019/8/17 18:03
 * @Description:
 */
@Component
public class NodeCache {
    private static Logger logger = LoggerFactory.getLogger(NodeCache.class);

    @Autowired
    private StakingMapper stakingMapper;

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
        String msgTpl = "构造缓存错误:ERR, 无法向其关联BUS_TYPE(stakingBlockNumber=SBN)";
        for (CustomStaking staking:stakingList){
            try {
                addStaking(staking);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException(msgTpl.replace("ERR",e.getMessage())
                        .replace("BUS_TYPE","质押")
                        .replace("SBN",staking.getStakingBlockNum().toString()));
            }
        }
        for (CustomDelegation delegation:delegationList){
            try {
                addDelegation(delegation);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException(msgTpl.replace("ERR",e.getMessage())
                        .replace("BUS_TYPE","委托")
                        .replace("SBN",delegation.getStakingBlockNum().toString()));
            }
        }
        for (CustomUnDelegation unDelegation:unDelegationList){
            try {
                addUnDelegation(unDelegation);
            } catch (NoSuchBeanException e) {
                throw new CacheConstructException(msgTpl.replace("ERR",e.getMessage())
                        .replace("BUS_TYPE","解委托")
                        .replace("SBN",unDelegation.getStakingBlockNum().toString()));
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
        if(staking==null){
            logger.info("【nodeId={},stakingBlockNumber={}】的质押在缓存中不存在,将从数据库查询...",nodeId,stakingNumber);
            StakingKey stakingKey = new StakingKey();
            stakingKey.setNodeId(nodeId);
            stakingKey.setStakingBlockNum(stakingNumber);
            Staking dbStaking = stakingMapper.selectByPrimaryKey(stakingKey);
            if(dbStaking==null) throw new NoSuchBeanException("【nodeId="+nodeId+",stakingBlockNumber="+stakingNumber+"】的质押不存在!");
            staking = new CustomStaking();
            BeanUtils.copyProperties(dbStaking,staking);
            if(CustomStaking.StatusEnum.CANDIDATE.getCode()==staking.getStatus()||CustomStaking.StatusEnum.EXITING.getCode()==staking.getStatus()){
                // 如果是候选中或退出中状态，则重新加入缓存
                addStaking(staking);
            }
        }
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
        return stakingSet.stream()
                .filter(staking -> statuses.contains(staking.getStatusEnum()))
                .collect(Collectors.toList());
    }

    /**
     * 获取指定状态的委托信息
     */
    public List<CustomDelegation> getDelegationByIsHistory(CustomDelegation.YesNoEnum... statusArr){
        List<?> statuses = Arrays.asList(statusArr);
        return delegationSet.stream()
                .filter(delegation -> statuses.contains(delegation.getIsHistoryEnum()))
                .collect(Collectors.toList());
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
    @SuppressWarnings("unlikely-arg-type")
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
                            .filter(unDelegation -> CustomUnDelegation.StatusEnum.EXITED.getCode()==unDelegation.getStatus())
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
