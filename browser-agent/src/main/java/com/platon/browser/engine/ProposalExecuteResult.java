package com.platon.browser.engine;

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
    // 全量数据，需要根据业务变化，保持与数据库一致
    private Map<String,Proposal> proposals = new HashMap<>();

    // 插入或更新数据
    private List<Vote> addVotes = new ArrayList<>();
    private Set<Proposal> addProposals = new HashSet<>();
    private Set<Proposal> updateProposals = new HashSet<>();
}
