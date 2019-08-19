package com.platon.browser.engine.result;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
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
}
