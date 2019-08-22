package com.platon.browser.engine.result;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.mapper.CustomProposalMapper;
import com.platon.browser.dto.CustomDelegation;
import com.platon.browser.dto.CustomProposal;
import com.platon.browser.dto.CustomVote;
import lombok.Data;

import java.util.*;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/10 16:47
 * @Description:
 */
@Data
public class ProposalExecuteResult {
    // 插入或更新数据
    private Set<Proposal> addProposals = new HashSet<>();
    private Set<Proposal> updateProposals = new HashSet<>();
    private Set<Vote> addVotes = new HashSet<>();
    public void clear() {
        addProposals.clear();
        updateProposals.clear();
        addVotes.clear();
    }

    /**
     * 把新增治理暂存至待新增入库列表
     * @param proposal
     */
    public void stageAddProposals( Proposal proposal){
        addProposals.add(proposal);
    }

    /**
     * 把更新后的治理暂存至待更新入库列表
     * @param proposal
     */
    public void stageUpdateProposals(Proposal proposal){
        updateProposals.add(proposal);
    }

    /**
     * 把新增治理暂存至待新增入库列表
     * @param proposal
     */
    public void stageAddVotes( CustomVote proposal){
        addVotes.add(proposal);
    }

}
