package com.platon.browser.dao.mapper;

import com.platon.browser.bean.CustomVote;
import com.platon.browser.bean.CustomVoteProposal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CustomVoteMapper {
    List<CustomVote> selectAll ();

    CustomVoteProposal selectVotePropal ( String hash );
}
