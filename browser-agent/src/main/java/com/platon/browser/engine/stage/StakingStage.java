package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.*;
import com.platon.browser.dto.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 质押新增或修改暂存类，入库后各容器需要清空
 */
public class StakingStage {
    // 插入或更新数据
    private Set<CustomNode> nodeInsertStage = new HashSet<>();
    private Set<CustomNode> nodeUpdateStage = new HashSet<>();
    private Set<CustomStaking> stakingInsertStage = new HashSet<>();
    private Set<CustomStaking> stakingUpdateStage = new HashSet<>();
    private Set<CustomDelegation> delegationInsertStage = new HashSet<>();
    private Set<CustomDelegation> delegationUpdateStage = new HashSet<>();
    private Set<CustomUnDelegation> unDelegationInsertStage = new HashSet<>();
    private Set<CustomUnDelegation> unDelegationUpdateStage = new HashSet<>();
    private Set<CustomSlash> slashInsertStage = new HashSet<>();
    private Set<CustomSlash> slashUpdateStage = new HashSet<>();
    private Set<CustomNodeOpt> nodeOptInsertStage = new HashSet<>();
    private Set<CustomNodeOpt> nodeOptUpdateStage = new HashSet<>();

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


    public void insertNode(CustomNode node){
        nodeInsertStage.add(node);
    }

    public void updateNode(CustomNode node){
        nodeUpdateStage.add(node);
    }

    public void insertStaking(CustomStaking staking){
        stakingInsertStage.add(staking);
    }
    public void insertStaking(CustomStaking staking,CustomTransaction tx){
        stakingInsertStage.add(staking);
        // 构建操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.TypeEnum.CREATE);
        nodeOpt.updateWithCustomTransaction(tx);
        // 暂存至待入库节点操作日志列表
        insertNodeOpt(nodeOpt);
    }
    public void updateStaking(CustomStaking staking){
        stakingUpdateStage.add(staking);
    }

    /**
     * 修改质押信息，并记录操作日志
     * @param staking
     * @param tx
     */
    public void modifyStaking(CustomStaking staking,CustomTransaction tx){
        stakingUpdateStage.add(staking);
        // 构建操作日志
        CustomNodeOpt nodeOpt = new CustomNodeOpt(staking.getNodeId(),CustomNodeOpt.TypeEnum.MODIFY);
        nodeOpt.updateWithCustomTransaction(tx);
        insertNodeOpt(nodeOpt);
    }

    public void insertDelegation(CustomDelegation delegation){
        delegationInsertStage.add(delegation);
    }
    public void updateDelegation(CustomDelegation delegation){
        delegationUpdateStage.add(delegation);
    }


    public void insertUnDelegation(CustomUnDelegation unDelegation){
        unDelegationInsertStage.add(unDelegation);
    }
    public void updateUnDelegation(CustomUnDelegation unDelegation){
        unDelegationUpdateStage.add(unDelegation);
    }

    public void insertSlash(CustomSlash slash){
        slashInsertStage.add(slash);
    }
    public void updateSlash(CustomSlash slash){
        slashUpdateStage.add(slash);
    }

    public void insertNodeOpt(CustomNodeOpt nodeOpt){
        nodeOptInsertStage.add(nodeOpt);
    }
    public void updateNodeOpt(CustomNodeOpt nodeOpt){
        nodeOptUpdateStage.add(nodeOpt);
    }

    public Set<CustomNode> getNodeInsertStage() {
        return nodeInsertStage;
    }
    public Set<CustomNode> getNodeUpdateStage() {
        return nodeUpdateStage;
    }
    public Set<CustomStaking> getStakingInsertStage() {
        return stakingInsertStage;
    }
    public Set<CustomStaking> getStakingUpdateStage() {
        return stakingUpdateStage;
    }
    public Set<CustomDelegation> getDelegationInsertStage() {
        return delegationInsertStage;
    }
    public Set<CustomDelegation> getDelegationUpdateStage() {
        return delegationUpdateStage;
    }
    public Set<CustomUnDelegation> getUnDelegationInsertStage() {
        return unDelegationInsertStage;
    }
    public Set<CustomUnDelegation> getUnDelegationUpdateStage() {
        return unDelegationUpdateStage;
    }
    public Set<CustomSlash> getSlashInsertStage() {
        return slashInsertStage;
    }
    public Set<CustomSlash> getSlashUpdateStage() {
        return slashUpdateStage;
    }
    public Set<CustomNodeOpt> getNodeOptInsertStage() {
        return nodeOptInsertStage;
    }
    public Set<CustomNodeOpt> getNodeOptUpdateStage() {
        return nodeOptUpdateStage;
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
}
