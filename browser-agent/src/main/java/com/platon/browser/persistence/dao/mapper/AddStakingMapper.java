package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.persistence.dao.param.AddStakingParam;
import org.springframework.transaction.annotation.Transactional;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface AddStakingMapper {
    /**
     * 增持质押
     */
    @Transactional
    void addStaking( AddStakingParam param );
}