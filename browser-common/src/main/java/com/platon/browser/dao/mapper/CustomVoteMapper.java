package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Vote;
import com.platon.browser.dao.entity.VoteExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomVoteMapper {

    int batchInsertOrUpdateSelective(@Param("list") Set<Vote> list, @Param("selective") Vote.Column... selective);
}
