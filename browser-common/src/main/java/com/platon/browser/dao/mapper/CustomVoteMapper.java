package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dto.CustomVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomVoteMapper {
    List<CustomVote> selectAll();
    int batchInsertOrUpdateSelective(@Param("list") Set<Vote> list, @Param("selective") Vote.Column... selective);
}
