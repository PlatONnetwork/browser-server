package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.*;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description:
 */
@Data
public class StakingStage {
    // 插入或更新数据
    private Set<Node> nodeInsertStage = new HashSet<>();
    private Set<Node> nodeUpdateStage = new HashSet<>();
    private Set<Staking> stakingInsertStage = new HashSet<>();
    private Set<Staking> stakingUpdateStage = new HashSet<>();
    private Set<Delegation> delegationInsertStage = new HashSet<>();
    private Set<Delegation> delegationUpdateStage = new HashSet<>();
    private Set<UnDelegation> unDelegationInsertStage = new HashSet<>();
    private Set<UnDelegation> unDelegationUpdateStage = new HashSet<>();
    private Set<Slash> slashInsertStage = new HashSet<>();
    private Set<Slash> slashUpdateStage = new HashSet<>();
    private Set<NodeOpt> nodeOptInsertStage = new HashSet<>();
    private Set<NodeOpt> nodeOptUpdateStage = new HashSet<>();

    /**
     * 清除待入库暂存空间
     */
    public void clear(){
        nodeInsertStage.clear();
        nodeUpdateStage.clear();
        stakingInsertStage.clear();
        stakingUpdateStage.clear();
        delegationInsertStage.clear();
        delegationUpdateStage.clear();
        unDelegationInsertStage.clear();
        unDelegationUpdateStage.clear();
        slashInsertStage.clear();
        slashUpdateStage.clear();
        nodeOptInsertStage.clear();
        nodeOptUpdateStage.clear();
    }


    public void insertNode(Node node){
        nodeInsertStage.add(node);
    }

    public void updateNode(Node node){
        nodeUpdateStage.add(node);
    }

    public void insertStaking(Staking staking){
        stakingInsertStage.add(staking);
    }
    public void insertStaking(Staking staking,CustomTransaction tx){
        stakingInsertStage.add(staking);
        if(tx!=null){
            // 如果是内置节点，暂存时是没有交易信息的，所以不用记日志
            // 构建操作日志
            CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.DescEnum.CREATE);
            nodeOpt.updateWithCustomTransaction(tx);
            // 暂存至待入库节点操作日志列表
            insertNodeOpt(nodeOpt);
        }
    }
    public void updateStaking(Staking staking){
        stakingUpdateStage.add(staking);
    }
    public void updateStaking(Staking staking,CustomTransaction tx){
        stakingUpdateStage.add(staking);
        // 构建操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.DescEnum.MODIFY);
        nodeOpt.updateWithCustomTransaction(tx);
        insertNodeOpt(nodeOpt);
    }

    public void insertDelegation(Delegation delegation){
        delegationInsertStage.add(delegation);
    }
    public void updateDelegation(Delegation delegation){
        delegationUpdateStage.add(delegation);
    }


    public void insertUnDelegation(UnDelegation unDelegation){
        unDelegationInsertStage.add(unDelegation);
    }
    public void updateUnDelegation(UnDelegation unDelegation){
        unDelegationUpdateStage.add(unDelegation);
    }

    public void insertSlash(Slash slash){
        slashInsertStage.add(slash);
    }
    public void updateSlash(Slash slash){
        slashUpdateStage.add(slash);
    }

    public void insertNodeOpt(NodeOpt nodeOpt){
        nodeOptInsertStage.add(nodeOpt);
    }
    public void updateNodeOpt(NodeOpt nodeOpt){
        nodeOptUpdateStage.add(nodeOpt);
    }
}
