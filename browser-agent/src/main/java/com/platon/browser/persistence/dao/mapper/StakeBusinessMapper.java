package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.common.complement.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface StakeBusinessMapper {
    /**
     * 发起质押
     * @param param
     */
    @Transactional
    void create(BusinessParam param);
    /**
     * 增持质押
     */
    @Transactional
    void increase(BusinessParam param);
    /**
     * 修改质押信息
     */
    @Transactional
    void modify(BusinessParam param );

    /**
     * 退出质押
     */
    @Transactional
    void exit(BusinessParam param);
}