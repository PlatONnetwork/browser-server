package com.platon.browser.complement.dao.mapper;

import com.platon.browser.complement.dao.param.BusinessParam;
import com.platon.browser.complement.dao.param.proposal.ProposalParameter;
import com.platon.browser.complement.dao.param.proposal.ProposalSlash;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface ProposalBusinessMapper {
    /**
     * 文本提案
     */
    @Transactional
    void text ( BusinessParam param );

    /**
     * 升级提案
     */
    @Transactional
    void upgrade ( BusinessParam param );

    /**
     * 取消提案
     */
    @Transactional
    void cancel ( BusinessParam param );

    /**
     * 投票
     */
    @Transactional
    void vote ( BusinessParam param );

    /**
     * 参数提案
     */
    @Transactional
    void parameter(ProposalParameter businessParam);
    
    /**
     * 提案数据更新
     */
    @Transactional
    void proposalSlashUpdate(@Param("proposalSlashs") List<ProposalSlash> proposalSlashs);
}