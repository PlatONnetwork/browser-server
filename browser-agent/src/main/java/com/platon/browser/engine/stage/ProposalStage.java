package com.platon.browser.engine.stage;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;

import java.util.HashSet;
import java.util.Set;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description: 提案新增或修改暂存类，入库后各容器需要清空
 */
public class ProposalStage {
    // 插入或更新数据
    private Set<Proposal> proposalInsertStage = new HashSet<>();
    private Set<Proposal> proposalUpdateStage = new HashSet<>();
    private Set<Vote> voteInsertStage = new HashSet<>();
    private Set<Vote> voteUpdateStage = new HashSet<>();
    public void clear() {
        proposalInsertStage.clear();
        proposalUpdateStage.clear();
        voteInsertStage.clear();
        voteUpdateStage.clear();
    }

    public void insertProposal( Proposal proposal){
        proposalInsertStage.add(proposal);
    }
    public void updateProposal( Proposal proposal){
        proposalUpdateStage.add(proposal);
    }

    public void insertVote( Vote vote){
        voteInsertStage.add(vote);
    }
    public void updateVote( Vote vote){
        voteUpdateStage.add(vote);
    }

    public Set<Proposal> exportProposal(){
        Set<Proposal> returnData = new HashSet<>(proposalInsertStage);
        returnData.addAll(proposalUpdateStage);
        return returnData;
    }

    public Set<Vote> exportVote(){
        Set<Vote> returnData = new HashSet<>(voteInsertStage);
        returnData.addAll(voteUpdateStage);
        return returnData;
    }

    public Set<Proposal> getProposalInsertStage() {
        return proposalInsertStage;
    }

    public Set<Proposal> getProposalUpdateStage() {
        return proposalUpdateStage;
    }

    public Set<Vote> getVoteInsertStage() {
        return voteInsertStage;
    }

    public Set<Vote> getVoteUpdateStage() {
        return voteUpdateStage;
    }
}
