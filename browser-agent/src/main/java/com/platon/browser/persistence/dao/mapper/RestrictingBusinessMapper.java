package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.common.complement.dto.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface RestrictingBusinessMapper {
    /**
     * 创建锁仓计划
     * @param param
     */
    @Transactional
    void create ( BusinessParam param );

}