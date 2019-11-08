package com.platon.browser.complement.dao.mapper;

import com.platon.browser.complement.dao.param.BusinessParam;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: dongqile
 * @Date: 2019/11/1
 * @Description:
 */
public interface SlashBusinessMapper {

    /**
     * 双签举报
     */
    @Transactional
    void report(BusinessParam param);
}