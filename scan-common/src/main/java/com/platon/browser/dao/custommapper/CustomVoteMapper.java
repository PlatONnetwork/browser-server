package com.platon.browser.dao.custommapper;

import com.platon.browser.bean.CustomVote;
import com.platon.browser.bean.CustomVoteProposal;

import java.util.List;

public interface CustomVoteMapper {
    List<CustomVote> selectAll ();

    CustomVoteProposal selectVotePropal ( String hash );
}
