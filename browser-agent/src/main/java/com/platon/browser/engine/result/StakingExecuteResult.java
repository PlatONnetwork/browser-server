package com.platon.browser.engine.result;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.*;
import com.platon.browser.engine.BlockChain;
import lombok.Data;

import java.math.BigInteger;
import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description:
 */
@Data
public class StakingExecuteResult {
    // 插入或更新数据
    private Set<Node> addNodes = new HashSet<>();
    private Set<Node> updateNodes = new HashSet<>();
    private Set<Staking> addStakings = new HashSet<>();
    private Set<Staking> updateStakings = new HashSet<>();
    private Set<Delegation> addDelegations = new HashSet<>();
    private Set<Delegation> updateDelegations = new HashSet<>();
    private Set<UnDelegation> addUnDelegations = new HashSet<>();
    private Set<UnDelegation> updateUnDelegations = new HashSet<>();
    private Set<Slash> addSlashs = new HashSet<>();
    private Set<NodeOpt> addNodeOpts = new HashSet<>();

    /**
     * 清除待入库暂存空间
     */
    public void clear(){
        addNodes.clear();
        updateNodes.clear();
        addStakings.clear();
        updateStakings.clear();
        addDelegations.clear();
        updateDelegations.clear();
        addSlashs.clear();
        addNodeOpts.clear();
    }

    /**
     * 把新增节点暂存至待新增入库列表
     * @param node
     */
    public void stageAddNode(CustomNode node){
        addNodes.add(node);
    }
    /**
     * 把更新后的节点暂存至待更新入库列表
     * @param node
     */
    public void stageUpdateNode(CustomNode node){
        updateNodes.add(node);
    }

    /**
     * 把新增质押暂存至待新增入库列表
     * @param staking
     * @param tx
     */
    public void stageAddStaking(CustomStaking staking,CustomTransaction tx){
        addStakings.add(staking);
        if(tx!=null){
            // 如果是内置节点，暂存时是没有交易信息的，所以不用记日志
            // 构建操作日志
            CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.DescEnum.CREATE);
            nodeOpt.updateWithCustomTransaction(tx);
            // 暂存至待入库节点操作日志列表
            stageAddNodeOpt(nodeOpt);
        }
    }

    /**
     * 把更新后的质押暂存至待更新入库列表
     * @param staking
     */
    public void stageUpdateStaking(CustomStaking staking){
        updateStakings.add(staking);
    }

    /**
     * 把更新后的质押暂存至待更新入库列表
     * @param staking
     * @param tx
     */
    public void stageUpdateStaking(CustomStaking staking,CustomTransaction tx){
        updateStakings.add(staking);
        // 构建操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.DescEnum.MODIFY);
        nodeOpt.updateWithCustomTransaction(tx);
        stageAddNodeOpt(nodeOpt);
    }

    /**
     * 把新增委托暂存至待新增入库列表
     * @param delegation
     */
    public void stageAddDelegation(CustomDelegation delegation){
        addDelegations.add(delegation);
    }

    /**
     * 把更新后的委托暂存至待更新入库列表
     * @param delegation
     */
    public void stageUpdateDelegation(CustomDelegation delegation){
        updateDelegations.add(delegation);
    }

    /**
     * 把新增撤销委托暂存至待新增入库列表
     * @param unDelegation
     */
    public void stageAddUnDelegation(CustomUnDelegation unDelegation){
        addUnDelegations.add(unDelegation);
    }

    /**
     * 把更新后的撤销委托暂存至待更新入库列表
     * @param unDelegation
     */
    public void stageUpdateUnDelegation(CustomUnDelegation unDelegation){
        updateUnDelegations.add(unDelegation);
    }

    /**
     * 把新增节点惩罚暂存至待新增入库列表
     * @param slash
     */
    public void stageAddSlash(CustomSlash slash){
        addSlashs.add(slash);
    }

    /**
     * 把新增节点操作日志暂存至待新增入库列表
     * @param nodeOpt
     */
    public void stageAddNodeOpt(CustomNodeOpt nodeOpt){
        addNodeOpts.add(nodeOpt);
    }
}
