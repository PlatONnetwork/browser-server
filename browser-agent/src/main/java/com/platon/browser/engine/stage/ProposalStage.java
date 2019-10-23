package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomVote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 提案新增或修改暂存类，入库后各容器需要清空
 */
public class ProposalStage {
    // 插入或更新数据
    private Set<CustomProposal> proposalInsertStage = new HashSet<>();
    private Set<CustomProposal> proposalUpdateStage = new HashSet<>();
    private Set<CustomVote> voteInsertStage = new HashSet<>();
    private Set<CustomVote> voteUpdateStage = new HashSet<>();
    public void clear() {
        proposalInsertStage.clear();
        proposalUpdateStage.clear();
        voteInsertStage.clear();
        voteUpdateStage.clear();
    }

    public void insertProposal( CustomProposal proposal){
        proposalInsertStage.add(proposal);
    }
    public void updateProposal( CustomProposal proposal){
        proposalUpdateStage.add(proposal);
    }

    public void insertVote( CustomVote vote){
        voteInsertStage.add(vote);
    }
    public void updateVote( CustomVote vote){
        voteUpdateStage.add(vote);
    }

    public Set<CustomProposal> getProposalInsertStage() {
        return proposalInsertStage;
    }
    public Set<CustomProposal> getProposalUpdateStage() {
        return proposalUpdateStage;
    }
    public Set<CustomVote> getVoteInsertStage() {
        return voteInsertStage;
    }
    public Set<CustomVote> getVoteUpdateStage() {
        return voteUpdateStage;
    }

    public Map<String, CustomProposal> exportProposalMap(){
        Map<String,CustomProposal> map = new HashMap<>();
        proposalInsertStage.forEach(e->map.put(e.getHash(),e));
        proposalUpdateStage.forEach(e->map.put(e.getHash(),e));
        return map;
    }
    public Set<Proposal> exportProposalSet(){
        Set<Proposal> returnData = new HashSet<>(proposalInsertStage);
        returnData.addAll(proposalUpdateStage);
        return returnData;
    }
    public Set<Vote> exportVoteSet(){
        Set<Vote> returnData = new HashSet<>(voteInsertStage);
        returnData.addAll(voteUpdateStage);
        return returnData;
    }
}
