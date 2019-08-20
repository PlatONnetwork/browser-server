package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Proposal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface CustomProposalMapper {

    int batchInsertOrUpdateSelective(@Param("list") Set<Proposal> list, @Param("selective") Proposal.Column... selective);
}
