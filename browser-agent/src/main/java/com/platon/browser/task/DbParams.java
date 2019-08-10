package com.platon.browser.task;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.Vote;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: dongqile
 * Date: 2019/8/10
 * Time: 18:16
 */
@Data
public class DbParams {
    private List <Vote> addVotes = new ArrayList <>();
    private Set <Proposal> addProposals = new HashSet <>();
    private Set<Proposal> updateProposals = new HashSet<>();

}