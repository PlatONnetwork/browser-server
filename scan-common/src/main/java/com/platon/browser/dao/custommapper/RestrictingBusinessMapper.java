package com.platon.browser.dao.custommapper;

import com.platon.browser.dao.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface RestrictingBusinessMapper {

    /**
     * 创建锁仓计划
     *
     * @param param
     */
    @Transactional(rollbackFor = {Exception.class, Error.class})
    void create(BusinessParam param);

}