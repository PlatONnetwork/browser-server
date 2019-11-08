package com.platon.browser.complement.mapper;

import com.platon.browser.common.complement.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface DelegateBusinessMapper {
    /**
     * 发起委托
     * @param param
     */
    @Transactional
    void create ( BusinessParam param );

    /**
     * 退出委托
     */
    @Transactional
    void exit ( BusinessParam param );
}