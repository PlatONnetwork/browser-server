package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.common.complement.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/11/5
 * @Description:
 */
public interface EpochBusinessMapper {
    /**
     * 新结算周期
     */
    @Transactional
    void settle ( BusinessParam param);

    /**
     * 新选举周期数据变更（结算&共识周期往前推20个块）
     */
    @Transactional
    void election (BusinessParam param);


    /**
     * 新共识周期数据变更
     */
    @Transactional
    void consensus(BusinessParam param);
}