package com.platon.browser.persistence.dao.mapper;

import com.platon.browser.persistence.dao.param.AddStakingParam;

/*
 * @Auther: dongqile
 * @Date:  2019/10/31
 * @Description:
 */
public interface AddStakingMapper {
    /**
     * 增持质押
     */
    void addStaking( AddStakingParam param );
}