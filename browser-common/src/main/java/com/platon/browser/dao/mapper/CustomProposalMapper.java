package com.platon.browser.dao.mapper;

import com.platon.browser.dao.entity.Proposal;
import com.platon.browser.dao.entity.ProposalExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CustomProposalMapper {

    int batchInsertOrUpdateSelective(@Param("list") Set<Proposal> list, @Param("selective") Proposal.Column... selective);
}
