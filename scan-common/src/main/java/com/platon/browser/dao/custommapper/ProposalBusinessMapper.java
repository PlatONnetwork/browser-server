package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.param.BusinessParam;
import com.platon.browser.dao.param.ppos.ProposalParameter;
import com.platon.browser.dao.param.ppos.ProposalSlash;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface ProposalBusinessMapper {

    /**
     * 文本提案
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void text(BusinessParam param);

    /**
     * 升级提案
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void upgrade(BusinessParam param);

    /**
     * 取消提案
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void cancel(BusinessParam param);

    /**
     * 投票
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void vote(BusinessParam param);

    /**
     * 参数提案
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void parameter(ProposalParameter businessParam);

    /**
     * 提案数据更新
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void proposalSlashUpdate(@Param("proposalSlashs") List<ProposalSlash> proposalSlashs);

}