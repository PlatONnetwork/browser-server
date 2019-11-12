package com.platon.browser.dao.mapper;//package com.platon.browser.dao.mapper.mapper_old;

import com.platon.browser.dto.CustomVote;
import com.platon.browser.dto.CustomVoteProposal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomVoteMapper {
    List<CustomVote> selectAll ();

    CustomVoteProposal selectVotePropal ( String hash );
}
