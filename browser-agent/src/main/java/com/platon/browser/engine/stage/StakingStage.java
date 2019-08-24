package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.*;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description:
 */
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
        // 构建操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.DescEnum.CREATE);
        nodeOpt.updateWithCustomTransaction(tx);
        // 暂存至待入库节点操作日志列表
        insertNodeOpt(nodeOpt);
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


    public Set<Node> exportNode(){
        Set<Node> returnData = new HashSet<>(nodeInsertStage);
        returnData.addAll(nodeUpdateStage);
        return returnData;
    }
    public Set<Staking> exportStaking(){
        Set<Staking> returnData = new HashSet<>(stakingInsertStage);
        returnData.addAll(stakingUpdateStage);
        return returnData;
    }
    public Set<Delegation> exportDelegation(){
        Set<Delegation> returnData = new HashSet<>(delegationInsertStage);
        returnData.addAll(delegationUpdateStage);
        return returnData;
    }
    public Set<UnDelegation> exportUnDelegation(){
        Set<UnDelegation> returnData = new HashSet<>(unDelegationInsertStage);
        returnData.addAll(unDelegationUpdateStage);
        return returnData;
    }
    public Set<Slash> exportSlash(){
        Set<Slash> returnData = new HashSet<>(slashInsertStage);
        returnData.addAll(slashUpdateStage);
        return returnData;
    }
    public Set<NodeOpt> exportNodeOpt(){
        Set<NodeOpt> returnData = new HashSet<>(nodeOptInsertStage);
        returnData.addAll(nodeOptUpdateStage);
        return returnData;
    }

    public Set<Node> getNodeInsertStage() {
        return nodeInsertStage;
    }

    public Set<Node> getNodeUpdateStage() {
        return nodeUpdateStage;
    }

    public Set<Staking> getStakingInsertStage() {
        return stakingInsertStage;
    }

    public Set<Staking> getStakingUpdateStage() {
        return stakingUpdateStage;
    }

    public Set<Delegation> getDelegationInsertStage() {
        return delegationInsertStage;
    }

    public Set<Delegation> getDelegationUpdateStage() {
        return delegationUpdateStage;
    }

    public Set<UnDelegation> getUnDelegationInsertStage() {
        return unDelegationInsertStage;
    }

    public Set<UnDelegation> getUnDelegationUpdateStage() {
        return unDelegationUpdateStage;
    }

    public Set<Slash> getSlashInsertStage() {
        return slashInsertStage;
    }

    public Set<Slash> getSlashUpdateStage() {
        return slashUpdateStage;
    }

    public Set<NodeOpt> getNodeOptInsertStage() {
        return nodeOptInsertStage;
    }

    public Set<NodeOpt> getNodeOptUpdateStage() {
        return nodeOptUpdateStage;
    }
}
