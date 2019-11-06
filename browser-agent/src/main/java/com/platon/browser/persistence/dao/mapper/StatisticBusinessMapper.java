package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.common.complement.dto.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface StatisticBusinessMapper {
    /**
     * 地址数据变更
     * @param param
     */
    @Transactional
    void addressChange ( BusinessParam param );

    /**
     * 统计数据变更
     * @param param
     */
    @Transactional
    void netWorkChange ( BusinessParam param );


}