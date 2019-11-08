package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.common.complement.param.BusinessParam;
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
}